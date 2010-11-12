package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.pedometer.StepDisplayer;
import j3.footpon.pedometer.StepService;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class FootponMapActivity extends MapActivity implements StepDisplayer
{
	MapView mapView;
	TextSwitcher pointView;
	
	FootponMapActivity footponMapActivity = this;
	ArrayList<Footpon> footpons;
	FootponMapActivity context = this;
	FootponItemizedOverlay footponOverlay;
	MyLocationOverlay myLocationOverlay;
	List<Overlay> mapOverlays;
	
	IFootponService service;
	StepService stepService;
	
	float point;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_map);
        
        context = this;
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        pointView = (TextSwitcher) findViewById(R.id.points);
        setAnimation(pointView);
        
        myLocationOverlay = getLocationOverlay(); 
        service = FootponServiceFactory.getService();

        //start and bind service... 
        //you have to control service by sending intent and set service connection for callback
        startService(new Intent(FootponMapActivity.this,
                StepService.class));
        bindStepService();
        
        
        MapController controller = mapView.getController();
        controller.setZoom(17);
        
    }

	private void setAnimation(TextSwitcher switcher) {
		Animation in = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in);
        Animation out = AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out);
        switcher.setInAnimation(in);
        switcher.setOutAnimation(out);
        
        switcher.setFactory(new ViewSwitcher.ViewFactory(){

			@Override
			public View makeView() {
				TextView t = new TextView(context);
		        t.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		        t.setTextSize(26);
		        t.setTextColor(Color.BLACK);
		        return t;
			}
        });
        
	}

	private MyLocationOverlay getLocationOverlay() {
		MyLocationOverlay overlay = new MyLocationOverlay(this, mapView);
		
		//handle footpons and map after received first position data
		overlay.runOnFirstFix(new Runnable() { public void run() {
            mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            GeoPoint currentPosition = myLocationOverlay.getMyLocation();
            Log.d(LOCATION_SERVICE, "current position:" +
            		currentPosition.getLatitudeE6()+" " +
            		currentPosition.getLongitudeE6());
            
            footpons = service.getFootponsInArea(currentPosition.getLatitudeE6(),
            									 currentPosition.getLongitudeE6());
            
            Drawable drawable = context.getResources().getDrawable(R.drawable.mark);
            mapOverlays = mapView.getOverlays();
            mapOverlays.add(myLocationOverlay);
            
            if(footpons.size() > 0){
            	footponOverlay = new FootponItemizedOverlay(drawable, context);
            	setMapItems(footponOverlay, drawable, footpons);
            	mapOverlays.add(footponOverlay);
            }
        }});
		overlay.enableMyLocation();
		return overlay;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.fp_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.search:
	    	startActivity(
	    			new Intent(footponMapActivity, CouponRedeemActivity.class));
	    	return true;
	    case R.id.list:
	    	startActivity(
	    			new Intent(footponMapActivity, FootponListActivity.class));
	    	return true;
	    case R.id.login:
	    	startActivity(new Intent(footponMapActivity, Login.class));
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setMapItems(FootponItemizedOverlay overlay, Drawable drawable, ArrayList<Footpon> footpons){
		
		for(Footpon fp:footpons)
		{
			GeoPoint point = new GeoPoint((int)(fp.getLatitude()* 1E6) ,(int)(fp.getLongitude()* 1E6));
	        OverlayItem oItem = new OverlayItem(point,fp.getStoreName(), 
	        		fp.getHiddenDescription() +"\npoints: " + 
	        		fp.getPointsRequired());
	        Drawable image;
	        //TODO: move this if-else nest to IconManager.getIcon(fp.getCategory());
	        if(fp.getCategory() == Footpon.CATAGORY_FOOD){
	        	image = context.getResources().getDrawable(R.drawable.icon_food);
	        }else if(fp.getCategory() == Footpon.CATAGORY_OUTDOOR){
	        	image = context.getResources().getDrawable(R.drawable.icon_outdoor);
	        }else if(fp.getCategory() == Footpon.CATAGORY_TOYS){
	        	image = context.getResources().getDrawable(R.drawable.icon_toys);
	        }else if(fp.getCategory() == Footpon.CATAGORY_VIDEO_GAME){
	        	image = context.getResources().getDrawable(R.drawable.icon_games);
	        }else{
	        	image = drawable;
	        }
	        boundCenterButtom(image);
	        oItem.setMarker(image);
			overlay.addOverlay(oItem);
		}
	}
	//TODO: move to IconManager.BoundCenterButtom
	private void boundCenterButtom(Drawable image) {
		image.setBounds(-image.getIntrinsicWidth() /2, -image.getIntrinsicHeight(),
						image.getIntrinsicWidth()/2, 0);
	}
	
	public void onStop() {
		super.onStop();
		unbindStepService();
		// Stop receiving location notifications.
		Log.i("Footpon", "stopping the listener");
		
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        bindStepService();
	}
	
	private void bindStepService() {
		Log.d(SENSOR_SERVICE, "Start binding service...");
		bindService(new Intent(FootponMapActivity.this, 
                StepService.class), connection, Context.BIND_AUTO_CREATE);
    }
	
	private void unbindStepService() {
        unbindService(connection);
    }
	
	private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            stepService = ((StepService.StepBinder) service).getService();
            stepService.setStepDisplayer(FootponMapActivity.this);
        }

        public void onServiceDisconnected(ComponentName className) {
        	stepService = null;
        }
    };

	@Override
	public void passValue(int steps, float points) {
		
		point = points;
		
		pointView.setText(String.valueOf(point));
	}
}
