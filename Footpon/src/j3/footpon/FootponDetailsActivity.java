package j3.footpon;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.model.IconHelper;
import j3.footpon.pedometer.StepService;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FootponDetailsActivity extends Activity {
	Button use;
	private TextView storeName;
	private TextView description;
	private TextView code;
	private TextView startDate;
	private TextView expireDate;
	private ImageView logo;
	private TextView steps;
	IFootponService service;
	private final String SHARE_USER_INF_TAG = "USER_INF_TAG";
	private String SHARE_USERNAME = "FOOTPON_USERNAME";
	
	long _id;
	String username;
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.footpon_details);
		service = FootponServiceFactory.getService();
		
		//init view
		getView();
		
		//get footpon data by footpon id or location from intent
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		boolean isRedeem = bundle.getBoolean("isRedeemed", false);
		_id = bundle.getLong("id");
		double _latitude = bundle.getDouble("latitude");
		double _longitude = bundle.getDouble("longitude");
		
		SharedPreferences share = getSharedPreferences(SHARE_USER_INF_TAG, 0);
		
		username = share.getString(SHARE_USERNAME, "");
	    
		Footpon footpon = null;
		if (_id != 0) {
			//footpon = service.getFootponById(_id);
			footpon=FootponServiceFactory.getService().getMyFootpons(username, _id);
		} else if (_latitude != 0 || _longitude != 0) {
			footpon = service.getFootponByLocation(_latitude, _longitude);
		} else{
			Toast.makeText(this, "no footpon data", 1000);
			return;
		}
		
        use=(Button) findViewById(R.id.use);  
        use.setOnClickListener(invalidate);
		
		//set footpon data
		setView(footpon);
	}
	
	private void getView(){
		storeName = (TextView) findViewById(R.id.detail_store_name);
		description = (TextView) findViewById(R.id.detail_description);
		code = (TextView) findViewById(R.id.code);
		startDate = (TextView) findViewById(R.id.detail_start_date);
		expireDate = (TextView) findViewById(R.id.detail_expire_date);
		logo = (ImageView) findViewById(R.id.logo);
		steps = (TextView) findViewById(R.id.detail_steps);
	}
	
	private void setView(Footpon footpon) {
		if(footpon != null){
			storeName.setText(footpon.getStoreName());
			description.setText(footpon.getRealDescription());
			startDate.setText(footpon.getStartDate());
			expireDate.setText(footpon.getEndDate());
			logo.setImageDrawable(IconHelper.getLogo(footpon.getStoreName(), this));
			steps.setText(String.valueOf(StepService.steps));

			if(footpon.getUsed())
			{
				code.setText("Already used");
		        use.setEnabled(false);
			}
			
			else
			{
				code.setText(Long.toString(footpon.getCode()));
				
				StringBuilder address=new StringBuilder("http://pdc-amd01.poly.edu/~jli15/footpon/barcode.php?upc=");
				address.append(Long.toString(footpon.getCode()));

				WebView mWebView;
				mWebView = (WebView) findViewById(R.id.webview);
				mWebView.getSettings().setJavaScriptEnabled(true);
				mWebView.loadUrl(address.toString());
			}
		}
	}
	
    public Button.OnClickListener invalidate = new Button.OnClickListener()
    {
    	public void onClick(View v)
    	{	
    		boolean temp=FootponServiceFactory.getService().invalidate(username, _id);
    		
    		if(temp==true)
    		{
    			finish();
    		}
    		
    		else
    		{
    		}
    	}
    };
}
