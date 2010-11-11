package j3.footpon.model;

import java.util.ArrayList;

public interface IFootponService {
	public ArrayList<Footpon> getFootponsInAreaServer(double longtitude,double latitude);
}
