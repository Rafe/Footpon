package j3.footpon.model;

import java.io.BufferedReader;
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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

import android.util.Log;

public class FootponService implements IFootponService {

	private static ArrayList<Footpon> _instance;
	
	//Code modified from http://www.helloandroid.com/tutorials/connecting-mysql-database.
<<<<<<< HEAD
	public ArrayList<Footpon> getFootponsInAreaServer(double currentLatitude, double currentLongitude)
	{
=======
	public ArrayList<Footpon> getFootponsInArea(double currentLatitude, double currentLongitude){
		
<<<<<<< HEAD
		//return cached result in memory
		if(_instance != null) return _instance;
		
>>>>>>> 19f83977ed013aada7209a8fd80e4e99eba4b2ab
=======
>>>>>>> f6bfb804a9b78921cb103150e6d955eebb5e95bf
		String result = "";
		InputStream is = null;
		
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double.toString(currentLatitude)));
		nameValuePairs.add(new BasicNameValuePair("currentLongitude", Double.toString(currentLongitude)));

		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		//http post
		try
		{
			HttpClient httpclient = new DefaultHttpClient();

			HttpPost httppost = new HttpPost("http://pdc-amd01.poly.edu/~jli15/footpon/getArea.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	
			HttpResponse response = httpclient.execute(httppost);
	
			HttpEntity entity = response.getEntity();
	
			is = entity.getContent();
		}

		catch(Exception ee)
		{
			Log.e("log_tag", "Error in http connection "+ee.toString());
		}

		//convert response to string
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	
			StringBuilder sb = new StringBuilder();
	
			String line = null;
	
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
	
			is.close();
	
			result=sb.toString();
		}

		catch(Exception eee)
		{
			Log.e("log_tag", "Error converting result "+eee.toString());
		}

		//parse json data
		try
		{
			JSONArray jArray = new JSONArray(result);
		
			for(int i=0;i<jArray.length();i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
			
				//Log.i("log_tag","Longitude: "+json_data.getDouble("longitude")+", Latitude: "+json_data.getDouble("latitude"));
				//int longitude=(int) (json_data.getDouble("longitude")*1000000);
				//int latitude=(int) (json_data.getDouble("latitude")*1000000);
				//String storeName=json_data.getString("storeName");
				//String hiddenDescription=json_data.getString("hiddenDescription");
				//String realDescription=json_data.getString("realDescription");
				//int pointsRequired =(int) json_data.getInt("pointsRequired");
				
		        //GeoPoint point = new GeoPoint(latitude, longitude);
		        //OverlayItem overlayitem = new OverlayItem(point, storeName, hiddenDescription+".\nPoints Required:"+pointsRequired);
		        
		        footpons.add(new Footpon(json_data));
		 
		        //itemizedoverlay.addOverlay(overlayitem);
		        //mapOverlays.add(itemizedoverlay);			
			}
		}

		catch(JSONException e)
		{
			Log.e("log_tag", "Error parsing data "+e.toString());
		}
		
		_instance = footpons;
		
		return footpons;
	}

	@Override
	public ArrayList<Footpon> getInstance() {
		if(_instance != null){ 
			return _instance;
		}else{ 
			return null;
		}
	}
	
	
}
