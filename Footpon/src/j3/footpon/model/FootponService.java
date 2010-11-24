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
		String result = "";
		InputStream is = null;

		// the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double
				.toString(currentLatitude)));
		nameValuePairs.add(new BasicNameValuePair("currentLongitude", Double
				.toString(currentLongitude)));

		ArrayList<Footpon> footpons = new ArrayList<Footpon>();

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost(
					"http://pdc-amd01.poly.edu/~jli15/footpon/getArea.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
			result = sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// parse json data
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

		return footpons;
	}

	@Override
	public ArrayList<Footpon> getMyFootpons() {
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		File sdcard=Environment.getExternalStorageDirectory();
		File file=new File(sdcard, "coupons.txt");

		int coupon_id=0;
		String result = "";
		InputStream is = null;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
	
		try
		{
			BufferedReader readerUserInf = new BufferedReader(new FileReader(file));
			
			coupon_id = readerUserInf.read()-48;
			
			while(coupon_id>=0)		// loop on the id of coupons user possess
			{
				nameValuePairs.add(new BasicNameValuePair("id", ""+coupon_id));
				
				//http post
				try
				{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost("http://pdc-amd01.poly.edu/~jli15/footpon/getCoupon.php");
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					is = entity.getContent();
				}
				catch(Exception e)
				{
					Log.e("log_tag", "Error in http connection "+e.toString());
				}
				
				//convert response to string
				try
				{
					BufferedReader readerCp = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
					
					StringBuilder sb = new StringBuilder();
					String line = null;
					
					while ((line = readerCp.readLine()) != null)
					{
						sb.append(line + "\n");
					}
					
					is.close();
					result=sb.toString();
				//	Log.e("log_tag", "result: "+result);
				}
				catch(Exception e)
				{
					Log.e("log_tag", "Error converting result "+e.toString());
				}
				
				//parse json data
				try
				{
					JSONArray jArray = new JSONArray(result);
					JSONObject json_data = jArray.getJSONObject(0);
					
					footpons.add(new Footpon(json_data));		
				}
				catch(JSONException e)
				{
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
				
				readerUserInf.read();
				coupon_id=readerUserInf.read()-48;
				Log.e("log_tag", "ID: "+coupon_id);
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
	public ArrayList<Footpon> getInstance() {
		
		if(_instance == null){
			_instance = getFootponsInArea(0,0);
		}
		return _instance;
	}
	
	@Override
	public boolean redeemFootpon(int userId,int footponId){
		return false;
	}
	
	@Override
	public boolean useFootpon(int userId,int footponId){
		return false;
	}
	
	@Override
	public boolean sync(int step){
		return false;
	}

	@Override
	public Footpon getFootponById(long id) {
		Footpon footpon = null;
		InputStream is = null;
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("id", "" + id));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://pdc-amd01.poly.edu/~jli15/footpon/getCoupon.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		} 
		catch (Exception ee) {
			Log.e("log_tag", "Error in http connection " + ee.toString());
		}

		// convert response to string
		try {
			BufferedReader readerCp = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = readerCp.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
		} 
		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);
			JSONObject json_data = jArray.getJSONObject(0);

			footpon = new Footpon(json_data);

		} 
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}

		return footpon;
	}

	@Override
	public Footpon getFootponByLocation(double longtitude, double latitude) {
		String result = "";
		InputStream is = null;
		Footpon footpon = null;
		
		// the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double
				.toString(latitude / 1000000)));
		nameValuePairs
				.add(new BasicNameValuePair("currentLongitude",
						Double.toString(longtitude / 1000000)));

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost("http://pdc-amd01.poly.edu/~jli15/footpon/getSingle.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();

			is = entity.getContent();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error in http connection " + e.toString());
		}

		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is, "iso-8859-1"), 8);

			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

			is.close();
			result = sb.toString();
		}
		catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
		}

		// parse json data
		try {
			JSONArray jArray = new JSONArray(result);

			JSONObject json_data = jArray.getJSONObject(0);

			footpon = new Footpon(json_data);
		}
		catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		
		return footpon;
	}
}
