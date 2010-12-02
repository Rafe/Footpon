package j3.footpon.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Footpon {
	
	private long id;
	private String storeName;
	private long code;
	private String category;
	private String hiddenDescription;
	private String realDescription;
	private double latitude;
	private double longitude;
	private long stepsRequired;
	private String startDate;
	private String endDate;
	private boolean used;
	
	public static final String CATEGORY_VIDEO_GAME="Video Game";
	public static final String CATEGORY_FOOD="Food";
	public static final String CATEGORY_TOYS="Toys";
	public static final String CATEGORY_OUTDOOR="Outdoor";
	
	public Footpon(long id, String storeName, String category, String hiddenDescription, 
			String realDescription, String startDate, String endDate, double latitude, 
			double longtitude, long stepsRequired, long code){
		this.id=id;
		this.storeName = storeName;
		this.category=category;
		this.hiddenDescription = hiddenDescription;
		this.realDescription = realDescription;
		this.startDate = startDate;
		this.endDate = endDate;
		this.latitude = latitude;
		this.longitude = longtitude;
		this.stepsRequired = stepsRequired;
		this.code=code;

	}
	
	public Footpon(JSONObject data) throws JSONException
	{
		this.id = data.getLong("id");
		this.storeName = data.getString("storeName");
		this.code = data.getLong("code");
		this.category = data.getString("category");
		this.hiddenDescription = data.getString("hiddenDescription");
		this.realDescription = data.getString("realDescription");
		this.category = data.getString("category");
		this.startDate = data.getString("startDate");
		this.endDate = data.getString("endDate");
		this.latitude = data.getDouble("latitude");
		this.longitude = data.getDouble("longitude");
		this.stepsRequired = data.getLong("stepsRequired");
		this.code = data.getLong("code");
		//this.used = data.getInt("used") == 0 ? true : false ;
	}
	
	public Footpon(JSONObject data, boolean mine) throws JSONException
	{
		this.id = data.getLong("id");
		this.storeName = data.getString("storeName");
		this.code = data.getLong("code");
		this.category = data.getString("category");
		this.hiddenDescription = data.getString("hiddenDescription");
		this.realDescription = data.getString("realDescription");
		this.category = data.getString("category");
		this.startDate = data.getString("startDate");
		this.endDate = data.getString("endDate");
		this.latitude = data.getDouble("latitude");
		this.longitude = data.getDouble("longitude");
		this.stepsRequired = data.getLong("stepsRequired");
		this.code = data.getLong("code");
		
		int temp=data.getInt("used");
		
		if(temp==0)
		{
			this.used=false;
		}
		
		else
		{
			this.used=true;
		}
	}
	
	//Getter and Setters...
	public long getID()
	{
		return id;
	}
	
	public void setID(long id)
	{
		this.id = id;
	}
	
	public String getStoreName()
	{
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

	public void setStepsRequired(long _requireSteps) {
		this.stepsRequired = _requireSteps;
	}

	public long getStepsRequired() {
		return stepsRequired;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndDate() {
		return endDate;
	}
	
	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean getUsed() {
		return used;
	}
}
