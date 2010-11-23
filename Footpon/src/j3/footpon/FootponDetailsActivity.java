package j3.footpon;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;

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

		IFootponService service = FootponServiceFactory.getService();

		TextView storeName = (TextView) findViewById(R.id.detail_store_name);
		TextView description = (TextView) findViewById(R.id.detail_real_description);
		TextView stepsRequired = (TextView) findViewById(R.id.detail_required_steps);
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
		description.setText(footpon.getRealDescription());
		expireDate.setText(footpon.getEndDate());
		stepsRequired.setText(Long.toString(footpon.getStepsRequired()));
	}

	public Footpon showByLocation(double latitude, double longitude) {
		// move code here
		return null;
	}
}
