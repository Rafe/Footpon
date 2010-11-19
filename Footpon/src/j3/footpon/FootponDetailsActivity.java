package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;

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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class FootponDetailsActivity extends Activity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.footpon_details);
        
        ArrayList<Footpon> footpons =FootponServiceFactory.getService().getInstance(); 
        
        TextView storeName = (TextView) findViewById(R.id.detail_store_name);
        TextView description = (TextView) findViewById(R.id.detail_real_description);
        TextView pointsRequired = (TextView) findViewById(R.id.detail_required_points);
        Button button = (Button) findViewById(R.id.Use);
        
        Footpon footpon=null;
        Intent i = getIntent();
        if(i != null)
        {    
    		String result = "";
    		InputStream is = null;

    		// the year data to send
    		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    		nameValuePairs.add(new BasicNameValuePair("currentLatitude", Double.toString(i.getExtras().getDouble("latitude")/1000000)));
    		nameValuePairs.add(new BasicNameValuePair("currentLongitude", Double.toString(i.getExtras().getDouble("longitude")/1000000)));



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
    			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

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

    			//for (int i = 0; i < jArray.length(); i++) {
    				JSONObject json_data = jArray.getJSONObject(0);

    				
    				//int longitude=(int) (json_data.getDouble("longitude")*1000000);
    				//int latitude=(int) (json_data.getDouble("latitude")*1000000);
    				//String storeName=json_data.getString("storeName");
    				//String hiddenDescription=json_data.getString("hiddenDescription");
    				//String realDescription=json_data.getString("realDescription");
    				//int pointsRequired =(int) json_data.getInt("pointsRequired");
//    				double latitude, double longtitude,int pointsRequired,long code){
    				footpon=new Footpon(json_data.getLong("id"), json_data.getString("storeName"), json_data.getString("category"), json_data.getString("hiddenDescription"), json_data.getString("realDescription"), (json_data.getDouble("latitude")*1000000), (json_data.getDouble("longitude")*1000000), (int) json_data.getInt("pointsRequired"), (long) json_data.getInt("code"));
    			//}
    		}

    		catch (JSONException e) {
    			Log.e("log_tag", "Error parsing data " + e.toString());
    		}
    		
        	//int position = i.getExtras().getInt("index");
        	//Footpon fp = footpons.get(position);
        	storeName.setText(footpon.getStoreName());
            description.setText(footpon.getRealDescription());
            //pointsRequired.setText(footpon.getPointsRequired()+" Points");
            
/*            if(i.getExtras().getBoolean("isRedeemed") == false){
            	button.setText("Redeem Now!");
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
            	    	startActivity(
            	    			new Intent(FootponDetailsActivity.this, Login.class));
                    }
                });
            }else{
            	
            }*/
            
        }
	}
	
}
