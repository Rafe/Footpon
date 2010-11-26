package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.model.IconHelper;
import j3.footpon.pedometer.StepService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FootponDetailsActivity extends Activity {
	private TextView storeName;
	private TextView description;
	private TextView stepsRequired;
	private TextView startDate;
	private TextView expireDate;
	private ImageView logo;
	private TextView steps;
	IFootponService service;
	
	
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
		long _id = bundle.getLong("id");
		double _latitude = bundle.getDouble("latitude");
		double _longitude = bundle.getDouble("longitude");
		
		Footpon footpon = null;
		if (_id != 0) {
			footpon = service.getFootponById(_id);
		} else if (_latitude != 0 || _longitude != 0) {
			footpon = service.getFootponByLocation(_latitude, _longitude);
		} else{
			Toast.makeText(this, "no footpon data", 1000);
			return;
		}
		
		//
		if(isRedeem){
			
		}
		
		//set footpon data
		setView(footpon);
	}
	
	private void getView(){
		storeName = (TextView) findViewById(R.id.detail_store_name);
		description = (TextView) findViewById(R.id.detail_description);
		stepsRequired = (TextView) findViewById(R.id.detail_required_steps);
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
			stepsRequired.setText(Long.toString(footpon.getStepsRequired()));
			logo.setImageDrawable(IconHelper.getLogo(footpon.getStoreName(), this));
			steps.setText(String.valueOf(StepService.steps));
		}
	}
}
