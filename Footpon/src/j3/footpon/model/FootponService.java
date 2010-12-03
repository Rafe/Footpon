package j3.footpon.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;

public class FootponService implements IFootponService {

	private static ArrayList<Footpon> _instance;

	@Override
	public ArrayList<Footpon> getFootponsInArea(double currentLatitude,
			double currentLongitude) {
		// the position data to send
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

		parameters.add(new BasicNameValuePair("currentLatitude", Double
				.toString(currentLatitude)));
		parameters.add(new BasicNameValuePair("currentLongitude", Double
				.toString(currentLongitude)));

		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		String result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/getArea.php", 
				parameters);
		
		JSONtoFootpons(result, footpons);
		
		//save data for List;
		_instance = footpons;
		
		return footpons;
	}
	
	@Override
	@Deprecated
	public ArrayList<Footpon> getMyFootpons() {
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		File sdcard=Environment.getExternalStorageDirectory();
		File file=new File(sdcard, "coupons.txt");

		int coupon_id=0;
		String result = "";
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
	
		try
		{
			BufferedReader readerUserInf = new BufferedReader(new FileReader(file));
			
			coupon_id = readerUserInf.read()-48;
			
			while(coupon_id>=0)		// loop on the id of coupons user possess
			{
				parameters.add(new BasicNameValuePair("id", ""+coupon_id));
				
				result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/getCoupon.php",
						parameters);
				
				//parse json data
				JSONtoFootpons(result, footpons);
				
				readerUserInf.read();
				coupon_id = readerUserInf.read()-48;
			//	Log.e("log_tag", "ID: "+coupon_id);
			}
		}
		catch (FileNotFoundException e)
		{
			Log.e("log_tag", "File not found. "+e.toString());
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	
		return footpons;
	}
	
	@Override
	public ArrayList<Footpon> getMyFootpons(String username) {
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("username", ""+username));
				
		String result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/getMyCoupon.php", parameters);
				
		//parse json data
		JSONtoFootpons(result, footpons, true);
				
		return footpons;
	}
	
	@Override
	public Footpon getMyFootpons(String username, long id) 
	{
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("username", ""+username));
		parameters.add(new BasicNameValuePair("id", ""+id));

		String result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/getMyCouponWithID.php", parameters);

		return JSONtoFootpon(result, true);
	}
	
	@Override
	public ArrayList<Footpon> getInstance() {
		
		if(_instance == null){
			//hardcode default position...
			_instance = getFootponsInArea(40.757942,-73.979478);
		}
		return _instance;
	}
	
	@Override
	public boolean redeemFootpon(String username,long footponId){
		//Data to send.
		String result = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",
				username));
		nameValuePairs.add(new BasicNameValuePair("id", Long
				.toString(footponId)));

		// http post
		result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/redeemCoupon.php",
					nameValuePairs);

		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);

			String success = json_data.getString("success");
			if (success.equalsIgnoreCase("true")){
				return true;
			}
		}catch (JSONException e) {
			Log.e("log_tag","Error parsing data " + e.toString());
		}
		
		return false;
	}
	
	@Override
	public boolean invalidate(String username, long id){
		//Data to send.
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username", username));
		nameValuePairs.add(new BasicNameValuePair("id", Long.toString(id)));

		// http post
		String result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/invalidate.php", nameValuePairs);

		// parse json data
		try 
		{
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);

			String success = json_data.getString("success");
			if (success.equalsIgnoreCase("true"))
			{
				return true;
			}
		}
		catch (JSONException e) 
		{
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		
		return false;
	}
	
	@Override
	public boolean sync(int step){
		return false;
	}

	@Override
	public Footpon getFootponById(long id) {
		String result = "";

		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", "" + id));

		result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/getCoupon.php",
				parameters);

		return JSONtoFootpon(result);
	}

	@Override
	public Footpon getFootponByLocation(double longtitude, double latitude) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double
				.toString(latitude / 1000000)));
		nameValuePairs
				.add(new BasicNameValuePair("currentLongitude",
						Double.toString(longtitude / 1000000)));

		String result = POST("http://pdc-amd01.poly.edu/~jli15/footpon/getSingle.php", 
				nameValuePairs);
			
		return JSONtoFootpon(result);
	}
	
	/*
	 * @parse result string and add footpon into arrayList
	 */
	private void JSONtoFootpons(String result, ArrayList<Footpon> footpons) {
		try {
			JSONArray jArray = new JSONArray(result);

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);

				footpons.add(new Footpon(json_data));
			}
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}
	
	private void JSONtoFootpons(String result, ArrayList<Footpon> footpons, boolean mine) {
		try {
			JSONArray jArray = new JSONArray(result);

			for (int i = 0; i < jArray.length(); i++) {
				JSONObject json_data = jArray.getJSONObject(i);

				footpons.add(new Footpon(json_data, mine));
			}
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
	}
	
	/*
	 * @parse result string and return single footpon
	 */
	private Footpon JSONtoFootpon(String result) {
		try {
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);

			return new Footpon(json_data);
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return null;
	}
	
	//@Jimmy: is the footpon have attribute "used" on every query?
	private Footpon JSONtoFootpon(String result, boolean mine) {
		try {
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);

			Footpon footpon=new Footpon(json_data);
			
			int temp=json_data.getInt("used");
			
			if(temp==0)
			{
				footpon.setUsed(false);
			}

			else
			{
				footpon.setUsed(true);
			}
			
			return footpon;
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return null;
	}
	
	private String POST(String url,ArrayList<NameValuePair> parameter){
		InputStream is = null;
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(new UrlEncodedFormEntity(parameter));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			is = entity.getContent();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			return sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}
		
		return null;
	}
}
