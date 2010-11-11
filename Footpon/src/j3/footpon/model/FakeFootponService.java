package j3.footpon.model;

import java.util.ArrayList;

public class FakeFootponService implements IFootponService {

	@Override
	public ArrayList<Footpon> getFootponsInAreaServer(double longtitude, double latitude) {
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
<<<<<<< HEAD
		//footpons.add(new Footpon("Nintendo World Store","10% NDS Game","Hidden",40.757942,-73.979478,40));
		//footpons.add(new Footpon("Wendy's","Free small soda","Hidden",40.758198,-73.981414,20));
		//footpons.add(new Footpon("Toys R Us","Buy one LEGO set, get second LEGO set half off","Hidden",40.757210,-73.985679,90));
		//footpons.add(new Footpon("Midtown Bikes","Buy one tire, get the second free","Hidden",40.761493,-73.990115,50));		
=======
		footpons.add(new Footpon("Nintendo World Store","10% NDS Game","Hidden",null, 40.757942,-73.979478,40,1234l));
		footpons.add(new Footpon("Wendy's","Free small soda","Hidden",null,40.758198,-73.981414,20, 0));
		footpons.add(new Footpon("Toys R Us","Buy one LEGO set, get second LEGO set half off","Hidden",null, 40.757210,-73.985679,90, 0));
		footpons.add(new Footpon("Midtown Bikes","Buy one tire, get the second free","Hidden",null, 40.761493,-73.990115,50, 0));		
>>>>>>> f6bfb804a9b78921cb103150e6d955eebb5e95bf
	
		return footpons;
	}
	
	@Override
	public ArrayList<Footpon> getInstance() {
			return getFootponsInArea(0,0);
	}
}
