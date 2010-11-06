package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponRepository;

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

public class FootponMapActivity extends MapActivity {

	MapView mapView;
	FootponMapActivity footponMapActivity = this;
	ArrayList<Footpon> footpons;
	
	//place for LocationService
	LocationManager locationManager;
	LocationListener locationListener;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        footpons = FootponRepository.getFootponsInArea(1, 0, 0, 100);
        //footpons = FootponRepository.getFootponsInAreaServer(1, 0, 0, 100);
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
        FootponItemizedOverlay footponOverlay = new FootponItemizedOverlay(drawable, this);
        
        
        setMapItems(footponOverlay, drawable, footpons);
        
        mapOverlays.add(footponOverlay);
        
        MapController controller = mapView.getController();
                setLocation(controller);
    }
	
	//setting current location and register location listener
	private void setLocation(final MapController controller) {
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationListener = new LocationListener() {

			@Override
			public void onLocationChanged(Location location) {
				try{
					GeoPoint currentPoint = new GeoPoint(
							(int)location.getLatitude()*1000000,
							(int)location.getLongitude()*1000000);
					controller.setCenter(currentPoint);
				}catch(Exception ex){
					Log.e(LOCATION_SERVICE, ex.getLocalizedMessage());
				}
			}

			@Override
			public void onProviderDisabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onProviderEnabled(String provider) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				// TODO Auto-generated method stub
				
			}
			
		};
		
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);
		
		/*GeoPoint poly = new GeoPoint(40757942,-73979478);

        controller.setCenter(poly);
        controller.setZoom(15);*/

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
	
	public void setMapItems(FootponItemizedOverlay overlay,Drawable drawable,ArrayList<Footpon> footpons){
		
		for(Footpon f : footpons){
			GeoPoint point = new GeoPoint((int)(f.getLatitude()* 1E6) ,(int)(f.getLongitude()* 1E6));
	        OverlayItem oItem = new OverlayItem(point,f.getStoreName(),f.getDescription() +"\npoints: " + f.getPointsRequired());
	        oItem.setMarker(drawable);
			overlay.addOverlay(oItem);
		}
		
	}
	
}