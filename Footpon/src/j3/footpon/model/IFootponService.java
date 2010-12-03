package j3.footpon.model;

import java.util.ArrayList;

//interface for Footpon Service
public interface IFootponService {
	public ArrayList<Footpon> getFootponsInArea(double latitude,double longitude);

	//return service instance, it's a singleton
	public ArrayList<Footpon> getInstance();
	public ArrayList<Footpon> getMyFootpons();
	public boolean redeemFootpon(String userName,long footponID);
	public boolean sync(int step);
	public Footpon getFootponById(long id);
	public Footpon getFootponByLocation(double longtitude,double latitude);
	public ArrayList<Footpon> getMyFootpons(String username);
	Footpon getMyFootpons(String username, long id);
	boolean invalidate(String username, long footponId);
}
