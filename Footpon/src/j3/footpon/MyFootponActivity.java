package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;

import java.util.ArrayList;

public class MyFootponActivity extends FootponListActivity {
	
	@Override
	protected void getFootponSource(String username) {
		
		ArrayList<Footpon> footpons = FootponServiceFactory.getService().getMyFootpons(username);
		
		if(footpons != null){
			showList(footpons);
		}
	}
}
