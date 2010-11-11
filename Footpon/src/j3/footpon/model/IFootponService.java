package j3.footpon.model;

import java.util.ArrayList;

public interface IFootponService {
	public ArrayList<Footpon> getFootponsInArea(double longtitude,double latitude);
	public ArrayList<Footpon> getInstance();
}
