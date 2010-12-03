package j3.footpon.test;

import java.util.ArrayList;

import com.google.android.maps.MapView;

import j3.footpon.FootponDetailsActivity;
import j3.footpon.FootponMapActivity;
import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import android.test.ActivityInstrumentationTestCase2;

public class FootponServiceTest extends 
	ActivityInstrumentationTestCase2<FootponDetailsActivity> {

	private IFootponService service;

	public FootponServiceTest(Class<FootponDetailsActivity> activityClass) {
		super(activityClass);
	}
	
	public FootponServiceTest() {
		super("j3.footpon",FootponDetailsActivity.class);
	}
	
	@Override
    protected void setUp() throws Exception {
        super.setUp();
        service = FootponServiceFactory.getService();
    }
	
	public void testServiceCanReturnFootpon(){
		Footpon fp = service.getFootponById(1);
		assertEquals(fp.getID(), 1);
	}
	
	public void testServiceCanGetFootponsInArea(){
		ArrayList<Footpon> footpons = service.getFootponsInArea(40.757942,-73.979478);
		assertTrue(footpons.size()>0);
	}
	
	public void testServiceCanGetMyFootpon(){
		ArrayList<Footpon> footpons = service.getMyFootpons("jimmychao");
		assertTrue(footpons.size()>0);
	}
	
	

}
