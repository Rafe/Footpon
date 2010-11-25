package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.model.IconHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FootponDetailsActivity extends Activity {
	private TextView storeName;
	private TextView rdescription;
	private TextView hdescription;
	private TextView stepsRequired;
	private TextView startDate;
	private TextView expireDate;
	private ImageView logo;
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
		}
		
		//set footpon data
		setView(footpon);
	}
	
	private void getView(){
		storeName = (TextView) findViewById(R.id.detail_store_name);
		rdescription = (TextView) findViewById(R.id.detail_real_description);
		hdescription = (TextView) findViewById(R.id.detail_hidden_description);
		stepsRequired = (TextView) findViewById(R.id.detail_required_steps);
		startDate = (TextView) findViewById(R.id.detail_start_date);
		expireDate = (TextView) findViewById(R.id.detail_expire_date);
		logo = (ImageView) findViewById(R.id.logo);
	}
	
	private void setView(Footpon footpon) {
		if(footpon != null){
			storeName.setText(footpon.getStoreName());
			rdescription.setText(footpon.getRealDescription());
			hdescription.setText(footpon.getHiddenDescription());
			startDate.setText(footpon.getStartDate());
			expireDate.setText(footpon.getEndDate());
			stepsRequired.setText(Long.toString(footpon.getStepsRequired()));
			logo.setImageDrawable(IconHelper.getLogo(footpon.getStoreName(), this));
		}
	}
}
