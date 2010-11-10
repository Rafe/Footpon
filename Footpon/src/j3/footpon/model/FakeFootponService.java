package j3.footpon.model;

import java.util.ArrayList;

public class FakeFootponService implements IFootponService {

	@Override
	public ArrayList<Footpon> getFootponsInArea(double longtitude, double latitude) {
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		footpons.add(new Footpon("Nintendo World Store","10% NDS Game","Hidden",40.757942,-73.979478,40));
		footpons.add(new Footpon("Wendy's","Free small soda","Hidden",40.758198,-73.981414,20));
		footpons.add(new Footpon("Toys R Us","Buy one LEGO set, get second LEGO set half off","Hidden",40.757210,-73.985679,90));
		footpons.add(new Footpon("Midtown Bikes","Buy one tire, get the second free","Hidden",40.761493,-73.990115,50));		
	
		return footpons;
	}

}
