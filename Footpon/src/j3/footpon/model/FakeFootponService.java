package j3.footpon.model;

import java.util.ArrayList;

public class FakeFootponService implements IFootponService {

	@Override
	public ArrayList<Footpon> getFootponsInArea(double longtitude, double latitude) {
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		footpons.add(new Footpon(1, "Nintendo World Store","Video Game","Hidden","10% NDS Game", "Oct 10", "Nov 1", 40.757942,-73.979478,40,1234l));
		footpons.add(new Footpon(2, "Wendy's","Food","Hidden","Free small soda","Oct 10", "Nov 1",40.758198,-73.981414,20, 0));
		footpons.add(new Footpon(3, "Toys R Us","Toys","Hidden","Buy one LEGO set, get second LEGO set half off","Oct 10", "Nov 1", 40.757210,-73.985679,90, 0));
		footpons.add(new Footpon(4, "Midtown Bikes","Outdoor","Hidden","Buy one tire, get the second free","Oct 10", "Nov 1", 40.761493,-73.990115,50, 0));		
	
		return footpons;
	}
	
	@Override
	public ArrayList<Footpon> getInstance() {
		return getFootponsInArea(0,0);
	}
	
	@Override
	public ArrayList<Footpon> getMyFootpons(){
		return getFootponsInArea(0,0);
	}
	
	@Override
	public boolean useFootpon(int userId,int footponId){
		return true;
	}
	
	@Override
	public boolean sync(int step){
		return false;
	}

	@Override
	public Footpon getFootponById(long id) {
		// TODO Auto-generated method stub
		return new Footpon(3, "Toys R Us","Toys","Hidden","Buy one LEGO set, get second LEGO set half off","Oct 10", "Nov 1", 40.757210,-73.985679,90, 0);
	}

	@Override
	public Footpon getFootponByLocation(double longtitude, double latitude) {
		// TODO Auto-generated method stub
		return new Footpon(3, "Toys R Us","Toys","Hidden","Buy one LEGO set, get second LEGO set half off","Oct 10", "Nov 1", 40.757210,-73.985679,90, 0);
	}

	@Override
	public boolean redeemFootpon(String userName, long footponId) {
		// TODO Auto-generated method stub
		return true;
	}
	
}
