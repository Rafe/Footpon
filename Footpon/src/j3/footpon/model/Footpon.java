package j3.footpon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Footpon {
	
	private String storeName;
	private long code;
	private String category;
	private String hiddenDescription;
	private String realDescription;
	private double latitude;
	private double longitude;
	private int pointsRequired;
	
	public Footpon(String storeName, String category, String hiddenDescription, String realDescription, double latitude, double longtitude,int pointsRequired,long code){
		this.storeName = storeName;
		this.category=category;
		this.hiddenDescription = hiddenDescription;
		this.realDescription = realDescription;
		this.latitude = latitude;
		this.longitude = longtitude;
		this.pointsRequired = pointsRequired;
		this.code=code;

	}
	
	public Footpon(JSONObject data) throws JSONException{
		
		this.storeName = data.getString("storeName");
		this.hiddenDescription = data.getString("hiddenDescription");
		this.realDescription = data.getString("realDescription");
		this.category = data.getString("category");
		this.latitude = data.getDouble("latitude");
		this.longitude = data.getDouble("longitude");
		this.pointsRequired = data.getInt("pointsRequired");
		this.code = data.getLong("code");
	}
	
	//Getter and Setters...
	public String getStoreName(){
		return storeName;
	}
	
	public void setStoreName(String storeName){
		this.storeName = storeName;
	}

	public void setCode(long code) {
		this.code = code;
	}

	public long getCode() {
		return code;
	}	
	
	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return category;
	}
	
	public void setHiddenDescription(String hiddenDescription) {
		this.hiddenDescription = hiddenDescription;
	}

	public String getHiddenDescription() {
		return hiddenDescription;
	}	
	
	public void setRealDescription(String realDescription) {
		this.realDescription = realDescription;
	}

	public String getRealDescription() {
		return realDescription;
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
