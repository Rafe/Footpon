package j3.footpon.model;

import java.util.ArrayList;

public interface IFootponService {
//<<<<<<< HEAD
	public ArrayList<Footpon> getFootponsInAreaServer(double longtitude,double latitude);
//=======
	public ArrayList<Footpon> getFootponsInArea(double longtitude,double latitude);
	public ArrayList<Footpon> getInstance();
//>>>>>>> f6bfb804a9b78921cb103150e6d955eebb5e95bf
}
