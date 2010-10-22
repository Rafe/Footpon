package j3.footpon.model;

public class Footpon {
	
	private String storeName;
	private String description;
	private double latitude;
	private double longitude;
	private int pointsRequired;
	
	public Footpon(String storeName,String description, double latitude,
					double longtitude,int pointsRequired){
		this.storeName = storeName;
		this.description = description;
		this.latitude = latitude;
		this.longitude = longtitude;
		this.pointsRequired = pointsRequired;
	}
	
	//Getter and Setters...
	public String getStoreName(){
		return storeName;
	}
	
	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setPointsRequired(int _requirePoints) {
		this.pointsRequired = _requirePoints;
	}

	public int getPointsRequired() {
		return pointsRequired;
	}
	
	
}
