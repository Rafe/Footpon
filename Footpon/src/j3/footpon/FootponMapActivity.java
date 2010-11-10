package j3.footpon;

import j3.footpon.model.FakeFootponService;
import j3.footpon.model.Footpon;
import j3.footpon.model.FootponRepository;
import j3.footpon.model.FootponService;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

import java.util.ArrayList;
import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FootponMapActivity extends MapActivity
{
	MapView mapView;
	FootponMapActivity footponMapActivity = this;
	ArrayList<Footpon> footpons;
	
	//place for LocationService
	LocationManager locationManager;
	LocationListener locationListener;
	FootponItemizedOverlay footponOverlay;
	
	IFootponService service;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.footpon_map);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        service = FootponServiceFactory.getService();
        //footpons = service.getFootponsInArea(40.757942,-73.979478);
        
		footpons = FootponRepository.getFootponsInAreaServer(40.75916,-73.984491);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.mark);
        footponOverlay = new FootponItemizedOverlay(drawable, this);
        
        setMapItems(footponOverlay, drawable, footpons);
        
        mapOverlays.add(footponOverlay);
        
        MapController controller = mapView.getController();
        
        setLocation(controller);
    }
	
	//setting current location and register location listener
	private void setLocation(final MapController controller) 
	{
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationListener = new LocationListener() 
		{
			@Override
			public void onLocationChanged(Location location) 
			{
				try
				{
					GeoPoint currentPoint = new GeoPoint(
							(int)(location.getLatitude()*1E6),
							(int)(location.getLongitude()*1E6)
							);
					
					controller.setCenter(currentPoint);
					setCurrentPositionItem(currentPoint);
				}
				
				catch(Exception ex)
				{
					Log.e(LOCATION_SERVICE, ex.getLocalizedMessage());
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) 
			{
				// TODO Auto-generated method stub	
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) 
			{
				// TODO Auto-generated method stub	
			}
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 100, locationListener);
		
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
	
	public void setCurrentPositionItem(GeoPoint point){
        OverlayItem oItem = new OverlayItem(point,"CurrentPosition","CurrentPosition");
        Drawable icon = this.getResources().getDrawable(R.drawable.icon); 
        oItem.setMarker(icon);
        footponOverlay.addOverlay(oItem);
	}
	
	public void onStop() {
		super.onStop();

		// Stop receiving location notifications.
		Log.i("Footpon", "stopping the listener");
		locationManager.removeUpdates(locationListener);

	}
	
}