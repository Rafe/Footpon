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

public class FootponRepository {

/*	public static ArrayList<Footpon> getFootponsInArea(int UserId,int latitude,int longtitude,int range){
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		footpons.add(new Footpon("Nintendo World Store","10% NDS Game",40.757942,-73.979478,40));
		footpons.add(new Footpon("Wendy's","Free small soda",40.758198,-73.981414,20));
		footpons.add(new Footpon("Toys R Us","Buy one LEGO set, get second LEGO set half off",40.757210,-73.985679,90));
		footpons.add(new Footpon("Midtown Bikes","Buy one tire, get the second free",40.761493,-73.990115,50));		
	
		return footpons;
		
	}*/
	
	//temporary method, wait to move
	//Code modified from http://www.helloandroid.com/tutorials/connecting-mysql-database.
	public static ArrayList<Footpon> getFootponsInAreaServer(double currentLatitude, double currentLongitude){
		
		String result = "";
		InputStream is = null;
		String ServiceUrl = "http://pdc-amd01.poly.edu/~jli15/footpon/getArea.php";
		
		//the year data to send
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	
		nameValuePairs.add(new BasicNameValuePair(Double.toString(currentLatitude), Double.toString(currentLongitude)));

		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		//receive data to inputStream
		try
		{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ServiceUrl);
			
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
		
		catch(Exception e)
		{
			Log.e("log_tag", "Error converting result "+e.toString());
		}

		//parse json data
		try
		{
			JSONArray jArray = new JSONArray(result);
		
			for(int i=0;i<jArray.length();i++)
			{
				JSONObject json_data = jArray.getJSONObject(i);
			
				//Log.i("log_tag","Longitude: "+json_data.getDouble("longitude")+", Latitude: "+json_data.getDouble("latitude"));
				int longitude=(int) (json_data.getDouble("longitude")*1000000);
				int latitude=(int) (json_data.getDouble("latitude")*1000000);
				String storeName=json_data.getString("storeName");
				String hiddenDescription=json_data.getString("hiddenDescription");
				String realDescription=json_data.getString("realDescription");
				int pointsRequired =(int) json_data.getInt("pointsRequired");
				
		        GeoPoint point = new GeoPoint(latitude, longitude);
		        OverlayItem overlayitem = new OverlayItem(point, storeName, realDescription+".\nPoints Required:"+pointsRequired);
		        
		        footpons.add(new Footpon(json_data));
		 
		        //itemizedoverlay.addOverlay(overlayitem);
		        //mapOverlays.add(itemizedoverlay);			
			}
		}

		catch(JSONException e)
		{
			Log.e("log_tag", "Error parsing data "+e.toString());
		}

		return footpons;
	}
	
}
