package j3.footpon;

import j3.footpon.model.FootponServiceFactory;
public class MyFootponActivity extends FootponListActivity {
	
	@Override
	protected void getFootponSource(String username) {
		
		footpons = FootponServiceFactory.getService().getMyFootpons(username);
		
		if(footpons != null){
			showList(footpons);
		}
	}
}
