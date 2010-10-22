package j3.footpon.model;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class FootponRepository {

	public static ArrayList<Footpon> getFootponsInArea(int UserId,int latitude,int longtitude,int range){
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		footpons.add(new Footpon("Nintendo World Store","10% NDS Game",40.757942,-73.979478,40));
		footpons.add(new Footpon("Wendy's","Free small soda",40.758198,-73.981414,20));
		footpons.add(new Footpon("Toys R Us","Buy one LEGO set, get second LEGO set half off",40.757210,-73.985679,90));
		footpons.add(new Footpon("Midtown Bikes","Buy one tire, get the second free",40.761493,-73.990115,50));		
	
		return footpons;
		
	}
	
	//temporary method, wait to move
	public static ArrayList<Footpon> getFootponsInAreaServer(int userId,int latitude,int longtitude,int range){
		
		String result= null;
		InputStream is = null;
		String ServiceUrl = "http://pdc-amd01.poly.edu/~jli15/footpon/getArea.php";
		
		//setting parameters
		ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("UserId",String.valueOf(userId)));
		parameters.add(new BasicNameValuePair("Latitude",String.valueOf(latitude)));
		parameters.add(new BasicNameValuePair("Longitude",String.valueOf(longtitude)));
		parameters.add(new BasicNameValuePair("Range",String.valueOf(range)));
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		//receive data to inputStream
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(ServiceUrl);
			
			httppost.setEntity(new UrlEncodedFormEntity(parameters));
			
			HttpResponse response = httpclient.execute(httppost);
			is = response.getEntity().getContent();
			
			
		}catch(Exception e){
			Log.e("Conntection Error", "Conntection Error " + e.toString());
		}
		
		//convert response to string
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
		
			StringBuilder sb = new StringBuilder();
		
			String line = null;
		
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		
			is.close();
		
			result=sb.toString();
		}catch(Exception e){
			Log.e("String convert Error","String convert Error in FootponRepository " + e.toString());
		}
		
		try{
			JSONArray jArray = new JSONArray(result);
			
			for(int i=0;i<jArray.length();i++)
			{
				JSONObject data = jArray.getJSONObject(i);
			
				//Log.i("log_tag","Longitude: "+json_data.getDouble("longitude")+", Latitude: "+json_data.getDouble("latitude"));
				footpons.add(new Footpon(data));
				
			}
		}catch(Exception e){
			Log.e("JSON parse Error","JSON parse Error in FootponRepository " + e.toString());
		}
		return footpons;
	}
	
}
