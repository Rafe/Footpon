package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.model.IconHelper;
import j3.footpon.model.StepBinder;
import j3.footpon.pedometer.StepDisplayer;
import j3.footpon.pedometer.StepService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class FootponMapActivity extends MapActivity implements StepDisplayer, StepBinder
{
	MapView mapView;
	TextSwitcher stepView;
	
	ArrayList<Footpon> footpons;
	FootponMapActivity context = this;
	FootponItemizedOverlay footponOverlay;
	MyLocationOverlay myLocationOverlay;
	
	IFootponService service;
	StepService stepService;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_map);
        
        getView();
        
        setAnimation(stepView);
        mapView.setBuiltInZoomControls(true);
        
        myLocationOverlay = getLocationOverlay(); 
        service = FootponServiceFactory.getService();
        
        //start and bind step service... 
        //you have to control service by sending intent and set service connection for callback
        startService(new Intent(FootponMapActivity.this, StepService.class));
        bindStepService();
        
        mapView.getController().setZoom(17);
    }

	private void getView() {
		mapView = (MapView) findViewById(R.id.mapview);
        stepView = (TextSwitcher) findViewById(R.id.steps);
        context = this;
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
	
	/*
	 * @set current location and load footpons by current location 
	 */
	private MyLocationOverlay getLocationOverlay() {
		MyLocationOverlay overlay = new MyLocationOverlay(this, mapView);
		
		//handle footpons and map after received first position data
		overlay.runOnFirstFix(new Runnable() { public void run() {
            mapView.getController().animateTo(myLocationOverlay.getMyLocation());
            GeoPoint currentPosition = myLocationOverlay.getMyLocation();
            Log.d(LOCATION_SERVICE, "current position:" +
            		currentPosition.getLatitudeE6()+" " +
            		currentPosition.getLongitudeE6());
			
            footpons = service.getFootponsInArea((double)currentPosition.getLatitudeE6()/1000000,
            									 (double)currentPosition.getLongitudeE6()/1000000);
            
            List<Overlay> mapOverlays = mapView.getOverlays();
            Drawable drawable = context.getResources().getDrawable(R.drawable.mark);
            mapOverlays = mapView.getOverlays();
            mapOverlays.add(myLocationOverlay);
            
            if(footpons.size() > 0){
            	footponOverlay = new FootponItemizedOverlay(drawable, context);
            	footponOverlay.setContext(context);
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
		    case R.id.stopService:
		    	stopStepService();
		    	return true;
		    default:
		        return super.onOptionsItemSelected(item);
	    }
	}
	
    private void stopStepService() {
    	stopService(new Intent(FootponMapActivity.this,
                StepService.class));
	}
	
	//pass footponList and overlay and default drawable icon, set MapItem on map
	public void setMapItems(FootponItemizedOverlay overlay, Drawable drawable, ArrayList<Footpon> footpons){
		
		for(Footpon fp:footpons)
		{
			GeoPoint point = new GeoPoint((int)(fp.getLatitude()* 1E6) ,(int)(fp.getLongitude()* 1E6));
	        OverlayItem oItem = new OverlayItem(point,fp.getStoreName(), 
	        		fp.getHiddenDescription() +"\nSteps: " + 
	        		fp.getStepsRequired());
	        
	        Drawable image = IconHelper.getMapIcon(fp.getCategory(), context);
	        oItem.setMarker(image);
			overlay.addOverlay(oItem);
		}
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        bindStepService();
	}
	
	@Override
	public void bindStepService() {
		Log.d(SENSOR_SERVICE, "Start binding service...");
		//use getApplicationContext().bindService when bindService in Tab
		//see http://code.google.com/p/android/issues/detail?id=2483
		boolean connected = getApplicationContext().bindService(new Intent(this, 
                StepService.class), connection, Context.BIND_AUTO_CREATE);
		if(!connected){
			Toast.makeText(context, "connected service failed", 1000);
		}
    }
	
	//unbind step service, called when application stop
	@Override
	public void unbindStepService() {
		getApplicationContext().unbindService(connection);
    }
	
	//the connection that access the step service
	private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //IMPORTANT: get service instance:
        	stepService = ((StepService.StepBinder) service).getService();
            stepService.setStepDisplayer(FootponMapActivity.this);
        }

        public void onServiceDisconnected(ComponentName className) {
        	stepService = null;
        }
    };
    
	@Override
	public void passValue(long steps, long currentSteps)
	{
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
