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
import android.widget.Button;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class FootponMapActivity extends MapActivity implements StepDisplayer
{
	Button search;
	Button myCoupon;
	Button account;
	
	MapView mapView;
	TextSwitcher stepView;
	
	ArrayList<Footpon> footpons;
	FootponMapActivity context = this;
	FootponItemizedOverlay footponOverlay;
	MyLocationOverlay myLocationOverlay;
	
	IFootponService service;
	StepService stepService;
	
	//float point;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_map);
        
        context = this;
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        stepView = (TextSwitcher) findViewById(R.id.steps);
        setAnimation(stepView);
        
        myLocationOverlay = getLocationOverlay(); 
        service = FootponServiceFactory.getService();

        //start and bind service... 
        //you have to control service by sending intent and set service connection for callback
        startService(new Intent(FootponMapActivity.this, StepService.class));
        bindStepService();
        
        
        MapController controller = mapView.getController();
        controller.setZoom(17);
        
        
        
        myCoupon=(Button) findViewById(R.id.myCouponButton);  
        myCoupon.setOnClickListener(getMyCoupon);  
        
        account=(Button) findViewById(R.id.accountButton);  
        account.setOnClickListener(getAccount);
    }
	
	// set animation for step changes : not necessary
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
			
        	footpons=new ArrayList<Footpon>();
            footpons = service.getFootponsInArea((double)currentPosition.getLatitudeE6()/1000000,
            									 (double)currentPosition.getLongitudeE6()/1000000);
            
            List<Overlay> mapOverlays = mapView.getOverlays();
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
	    	
	    	return true;
	    case R.id.list:
	    	startActivity(
	    			new Intent(context, FootponListActivity.class));
	    	return true;
	    case R.id.showInformation:
	    	startActivity(new Intent(context, ShowInformation.class));
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
    private View.OnClickListener getMyCoupon = new View.OnClickListener()
    {
		@Override
		public void onClick(View v) 
		{
			Intent intent=new Intent(context, FootponListActivity.class);
			startActivity(intent);
		}
    };
	
    private View.OnClickListener getAccount = new View.OnClickListener()
    {
		@Override
		public void onClick(View v) 
		{
			Intent intent=new Intent(context, ShowInformation.class);
			startActivity(intent);
		}
    };
	
	//pass footponList and overlay and default drawable icon, set MapItem on map
	public void setMapItems(FootponItemizedOverlay overlay, Drawable drawable, ArrayList<Footpon> footpons){
		
		for(Footpon fp:footpons)
		{
			GeoPoint point = new GeoPoint((int)(fp.getLatitude()* 1E6) ,(int)(fp.getLongitude()* 1E6));
	        OverlayItem oItem = new OverlayItem(point,fp.getStoreName(), 
	        		fp.getHiddenDescription() +"\nSteps: " + 
	        		fp.getStepsRequired());
	        Drawable image;
	        //TODO: move this if-else nest to IconManager.getIcon(fp.getCategory());
	        if(fp.getCategory().equals(Footpon.CATEGORY_FOOD)){
	        	image = context.getResources().getDrawable(R.drawable.icon_food);
	        }else if(fp.getCategory().equals(Footpon.CATEGORY_OUTDOOR)){
	        	image = context.getResources().getDrawable(R.drawable.icon_outdoor);
	        }else if(fp.getCategory().equals(Footpon.CATEGORY_TOYS)){
	        	image = context.getResources().getDrawable(R.drawable.icon_toys);
	        }else if(fp.getCategory().equals(Footpon.CATEGORY_VIDEO_GAME)){
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
	
	//unbind step service, called when application stop
	private void unbindStepService() {
        unbindService(connection);
    }
	
	//the connection that access the step service
	//TODO: get steps data from connection?
	private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            stepService = ((StepService.StepBinder) service).getService();
            stepService.setStepDisplayer(FootponMapActivity.this);
        }

        public void onServiceDisconnected(ComponentName className) {
        	stepService = null;
        }
    };
    
    //
	@Override
	public void passValue(long steps, long currentSteps)//, float points) {
	{
		//point = points;
		
		stepView.setText(String.valueOf(steps));
	}
	
	@Override
    public void onPause() 
    {
		myLocationOverlay.disableMyLocation();
		super.onStop();
    }
	
	@Override
    public void onStop() 
    {
		myLocationOverlay.disableMyLocation();
    	super.onStop();
    	
		unbindStepService();
		// Stop receiving location notifications.
		Log.i("Footpon", "stopping the listener");
    }
}
