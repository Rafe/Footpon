package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FootponDetailsActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.footpon_details);

		IFootponService service = FootponServiceFactory.getService();

		TextView storeName = (TextView) findViewById(R.id.detail_store_name);
		TextView rdescription = (TextView) findViewById(R.id.detail_real_description);
		TextView hdescription = (TextView) findViewById(R.id.detail_hidden_description);
		TextView stepsRequired = (TextView) findViewById(R.id.detail_required_steps);
		TextView startDate = (TextView) findViewById(R.id.detail_start_date);
		TextView expireDate = (TextView) findViewById(R.id.detail_expire_date);

		Footpon footpon = null;
		long _id;
		double _latitude, _longitude;

		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		_id = bundle.getLong("id");
		_latitude = bundle.getDouble("latitude");
		_longitude = bundle.getDouble("longitude");

		if (_id != 0) {
			footpon = service.getFootponById(_id);
		} else if (_latitude != 0 || _longitude != 0) {
			footpon = service.getFootponByLocation(_latitude, _longitude);
		}

		storeName.setText(footpon.getStoreName());
		rdescription.setText(footpon.getRealDescription());
		hdescription.setText(footpon.getHiddenDescription());
		startDate.setText(footpon.getStartDate());
		expireDate.setText(footpon.getEndDate());
		stepsRequired.setText(Long.toString(footpon.getStepsRequired()));
	}
}
