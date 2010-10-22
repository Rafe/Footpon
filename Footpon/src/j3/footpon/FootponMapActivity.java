package j3.footpon;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class FootponMapActivity extends MapActivity {

	MapView mapView;
	FootponMapActivity footponMapActivity = this;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);
        
        List<Overlay> mapOverlays = mapView.getOverlays();
        Drawable drawable = this.getResources().getDrawable(R.drawable.icon);
        FootponItemizedOverlay footponOverlay = new FootponItemizedOverlay(drawable, this);
        
        GeoPoint poly = new GeoPoint(40692882 ,-73984399);
        OverlayItem oItem = new OverlayItem(poly,"poly","this is a test overlay");
        oItem.setMarker(drawable);
        footponOverlay.addOverlay(oItem);
        mapOverlays.add(footponOverlay);
        
        MapController controller = mapView.getController();
        controller.setCenter(poly);
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
	
}