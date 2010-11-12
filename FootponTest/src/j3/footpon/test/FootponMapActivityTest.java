package j3.footpon.test;

import com.google.android.maps.MapView;

import j3.footpon.FootponMapActivity;
import android.test.ActivityInstrumentationTestCase2;

public class FootponMapActivityTest extends
		ActivityInstrumentationTestCase2<FootponMapActivity> {
	
	private FootponMapActivity mActivity;
	private MapView mapView;

	public FootponMapActivityTest(){
		super("j3.footpon",FootponMapActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        mapView = (MapView) mActivity.findViewById(j3.footpon.R.id.mapview);
    }
	
	public void testMainActivityHasMapView(){
		assertNotNull(mapView);
	}
	
	public void testActivityTitleIsFootpon(){
		assertEquals(mActivity.getTitle(),"Footpon");
	}
}
