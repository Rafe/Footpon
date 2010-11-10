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

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class FootponMapActivity extends MapActivity implements SensorEventListener
{
	MapView mapView;
	FootponMapActivity footponMapActivity = this;
	ArrayList<Footpon> footpons;
	
	LocationManager locationManager;
	LocationListener locationListener;
	
	FootponItemizedOverlay footponOverlay;
	MyLocationOverlay myLocationOverlay;
	
	IFootponService service;
	StepService stepService;
	
	float point;
	
	TextView pointView;
	SensorManager sensorManager;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        pointView = (TextView) findViewById(R.id.points);
        
        service = FootponServiceFactory.getService();
        footpons = service.getFootponsInArea(40.757942,-73.979478);
        
        stepService = new StepService();
        
        StepListener stepDisplayer = new StepListener(){

			@Override
			public void onStep() {
				point = point + 0.25f;
				pointView.setText(String.valueOf(point));
			}
        	
        };
        
        startService(new Intent(FootponMapActivity.this,
                StepService.class));
        stepService.registerListener(stepDisplayer);
        
        Drawable drawable = this.getResources().getDrawable(R.drawable.mark);
        footponOverlay = new FootponItemizedOverlay(drawable, this);
        
        setMapItems(footponOverlay, drawable, footpons);
        
        myLocationOverlay = new MyLocationOverlay(this, mapView);
        myLocationOverlay.runOnFirstFix(new Runnable() { public void run() {
            mapView.getController().animateTo(myLocationOverlay.getMyLocation());
        }});
        myLocationOverlay.enableMyLocation();
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        mapOverlays.add(footponOverlay);
        mapOverlays.add(myLocationOverlay);
        
        MapController controller = mapView.getController();
        controller.setZoom(17);
        
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
			GeoPoint point = new GeoPoint((int)(footpons.get(i).getLatitude()* 1E6) ,(int)(footpons.get(i).getLongitude()* 1E6));
	        OverlayItem oItem = new OverlayItem(point,footpons.get(i).getStoreName(), footpons.get(i).getHiddenDescription() +"\npoints: " + footpons.get(i).getPointsRequired());
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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
	}
    
}