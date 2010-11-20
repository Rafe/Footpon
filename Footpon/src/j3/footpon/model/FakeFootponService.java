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

public class FakeFootponService implements IFootponService {

	@Override
	public ArrayList<Footpon> getFootponsInArea(double longtitude, double latitude) {
		
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		footpons.add(new Footpon(1, "Nintendo World Store","Video Game","Hidden","10% NDS Game", 40.757942,-73.979478,40,1234l));
/*		footpons.add(new Footpon(2, "Wendy's","Food","Hidden","Free small soda",40.758198,-73.981414,20, 0));
		footpons.add(new Footpon(3, "Toys R Us","Toys","Hidden","Buy one LEGO set, get second LEGO set half off", 40.757210,-73.985679,90, 0));
		footpons.add(new Footpon(4, "Midtown Bikes","Outdoor","Hidden","Buy one tire, get the second free", 40.761493,-73.990115,50, 0));		
	*/
		return footpons;
	}
	
	public ArrayList<Footpon> getMyFootpons() {
		ArrayList<Footpon> footpons = new ArrayList<Footpon>();
		
		File sdcard=Environment.getExternalStorageDirectory();
		File file=new File(sdcard, "user.txt");

		int coupon_id=0;
		
		if(file.exists())
		{
			StringBuilder text=new StringBuilder();
			String result = "";
			InputStream is = null;
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		
			try
			{
				BufferedReader readerUserInf=new BufferedReader(new FileReader(file));
				String line;
				
				for(int i=0;i<6;i++)
				{
					line=readerUserInf.readLine();
					text.append(line);
					text.append('\n');
				}
				while(coupon_id>=0)
				{
					coupon_id=readerUserInf.read()-48;
					Log.e("log_tag", "ID: "+coupon_id);
					
					nameValuePairs.add(new BasicNameValuePair("id", "3"));
					
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
					catch(Exception ee)
					{
						Log.e("log_tag", "Error in http connection "+ee.toString());
					}
					
					//convert response to string
					try
					{
						BufferedReader readerCp = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
						
						StringBuilder sb = new StringBuilder();
						String lineCp = null;
						
						while ((lineCp = readerCp.readLine()) != null)
						{
							sb.append(lineCp + "\n");
						}
						
						is.close();
						result=sb.toString();
						Log.e("log_tag", "ID: "+result);
					}
					catch(Exception eee)
					{
						Log.e("log_tag", "Error converting result "+eee.toString());
					}
					
					//parse json data
					try
					{
						JSONArray jArray = new JSONArray(result);
	
						int i=0;
						JSONObject json_data = jArray.getJSONObject(i);
						
						long id=json_data.getLong("id");
						String storeName=json_data.getString("storeName");
						String hiddenDesc =json_data.getString("hiddenDescription");
						String realDesc=json_data.getString("realDescription");
						String category=json_data.getString("category");
						long pointsReq =json_data.getLong("pointsRequired");
						Log.e("log_tag", "ID: "+id);
						Log.e("log_tag", "storeName: "+storeName);
						Log.e("log_tag", "pointsRequired: "+pointsReq);
						Log.e("log_tag", "category: "+category);
						footpons.add(new Footpon(id, storeName, category, hiddenDesc, realDesc, 40.757942, -73.979478, 40, 1234l));		
					}
					catch(JSONException e)
					{
						Log.e("log_tag", "Error parsing data "+e.toString());
					}
				}
			}
			catch (FileNotFoundException e)
			{
				text.append("No information found.");
				text.append('\n');
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
			Log.e("log_tag", "File doesnot exist");
	
		return footpons;
	}
	
	@Override
	public ArrayList<Footpon> getInstance() {
	//	return getFootponsInArea(0,0);
		return getMyFootpons();
	}
}
