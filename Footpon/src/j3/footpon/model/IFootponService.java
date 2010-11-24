package j3.footpon.model;

import java.util.ArrayList;

//interface for Footpon Service
public interface IFootponService {
	public ArrayList<Footpon> getFootponsInArea(double longtitude,double latitude);

	//return service instance, it's a singleton
	public ArrayList<Footpon> getInstance();
	public ArrayList<Footpon> getMyFootpons();
	public boolean redeemFootpon(int userId,int footponId);
	public boolean useFootpon(int userId,int footponId);
	public boolean sync(int step);
	public Footpon getFootponById(long id);
	public Footpon getFootponByLocation(double longtitude,double latitude);
	
}
