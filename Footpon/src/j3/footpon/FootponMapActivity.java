package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.pedometer.StepListener;
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class FootponMapActivity extends MapActivity implements StepListener
{
	MapView mapView;
	FootponMapActivity footponMapActivity = this;
	ArrayList<Footpon> footpons;
	FootponMapActivity context = this;
	FootponItemizedOverlay footponOverlay;
	MyLocationOverlay myLocationOverlay;
	List<Overlay> mapOverlays;
	
	IFootponService service;
	StepService stepService;
	
	float point;
	
	TextView pointView;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_map);
        
        context = this;
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        pointView = (TextView) findViewById(R.id.points);
        
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
            footponOverlay = new FootponItemizedOverlay(drawable, context);

            setMapItems(footponOverlay, drawable, footpons);
            mapOverlays = mapView.getOverlays();
            
            mapOverlays.add(footponOverlay);
            mapOverlays.add(myLocationOverlay);
            
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
	    	//
	    	return true;
	    case R.id.list:
	    	Intent intent = new Intent(footponMapActivity,FootponListActivity.class);
	    	startActivity(intent);
	    	return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	public void setMapItems(FootponItemizedOverlay overlay, Drawable drawable, ArrayList<Footpon> footpons){
		
		for(int i=0; i<footpons.size(); i++)
		{
			Footpon fp = footpons.get(i);
			GeoPoint point = new GeoPoint((int)(fp.getLatitude()* 1E6) ,(int)(fp.getLongitude()* 1E6));
	        OverlayItem oItem = new OverlayItem(point,fp.getStoreName(), 
	        		fp.getHiddenDescription() +"\npoints: " + 
	        		fp.getPointsRequired());
	        //TODO: change this to catagory icon
	        oItem.setMarker(drawable);
			overlay.addOverlay(oItem);
		}
	}
	
	public void onStop() {
		super.onStop();
		
		// Stop receiving location notifications.
		Log.i("Footpon", "stopping the listener");
		
		//locationManager.removeUpdates(locationListener);

	}
	
	private void bindStepService() {
		Log.d(SENSOR_SERVICE, "Start binding service...");
		bindService(new Intent(FootponMapActivity.this, 
                StepService.class), connection, Context.BIND_AUTO_CREATE);
    }
	
	private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            stepService = ((StepService.StepBinder) service).getService();
            stepService.registerListener(FootponMapActivity.this);
            
        }

        public void onServiceDisconnected(ComponentName className) {
        	stepService = null;
        }
    };
    
	@Override
    protected void onResume() {
        super.onResume();
	}
	
	@Override
	public void onStep() {
		point = point + 0.25f;
		pointView.setText(String.valueOf(point));
		Log.d(SENSOR_SERVICE, "My one small step is a huge step for human...");
	}
    
	
}
