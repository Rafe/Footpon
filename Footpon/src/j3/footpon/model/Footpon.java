package j3.footpon.model;

public class Footpon {
	
	private String title;
	private String details;
	private double latitude;
	private double longitude;
	private int requirePoints;
	
	public Footpon(String title,String details, double latitude,
					double longtitude,int requirePoints){
		this.title = title;
		this.details = details;
		this.latitude = latitude;
		this.longitude = longtitude;
		this.requirePoints = requirePoints;
	}
	
	//Getter and Setters...
	public String getTitle(){
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}

	public void setDetails(String _details) {
		this.details = _details;
	}

	public String getDetails() {
		return details;
	}

	public void setLatitude(double _latitude) {
		this.latitude = _latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double _longitude) {
		this.longitude = _longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setRequirePoints(int _requirePoints) {
		this.requirePoints = _requirePoints;
	}

	public int getRequirePoints() {
		return requirePoints;
	}
	
	
}
