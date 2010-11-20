package j3.footpon.model;

import java.util.ArrayList;


//interface for Footpon Service
public interface IFootponService {
	public ArrayList<Footpon> getFootponsInArea(double longtitude,double latitude);

	//return service instance, it's a singleton
	public ArrayList<Footpon> getInstance();
	public ArrayList<Footpon> getMyFootpons(int userId);
	public boolean redeemFootpon(int userId,int footponId);
	public boolean useFootpon(int userId,int footponId);
	public boolean sync(int point);
	
}
