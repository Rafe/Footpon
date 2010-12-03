package j3.footpon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import j3.footpon.model.Footpon;
import j3.footpon.model.FootponServiceFactory;
import j3.footpon.model.IFootponService;
import j3.footpon.model.IconHelper;
import j3.footpon.model.StepBinder;
import j3.footpon.model.User;
import j3.footpon.pedometer.StepDisplayer;
import j3.footpon.pedometer.StepService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FootponDetailsActivity extends Activity implements StepDisplayer,StepBinder {
	private TextView storeName;
	private TextView description;
	private TextView code;
	private TextView expireDate;
	private ImageView logo;
	private TextView stepView;
	private IFootponService service;
	private WebView barcodeView;
	private ToggleButton use;
	private TextView stepsRequired;
	private StepService stepService;
	private Footpon footpon = null;
	
	long _steps;
	long _id;
	String username;
	boolean own_coupon = false;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.footpon_details);
		service = FootponServiceFactory.getService();
		
		getView();
		
		//get footpon data by footpon id or location from intent
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		own_coupon = bundle.getBoolean("own", true);
		_steps = bundle.getLong("steps",0);
		_id = bundle.getLong("id",0);
		
		SharedPreferences share = getSharedPreferences(User.SHARE_USER_INF_TAG, 0);
		username = share.getString(User.SHARE_USERNAME, "");
	    
		if (_id != 0) {
			footpon = service.getMyFootpons(username, _id);
		}else{
			Toast.makeText(this, "no footpon data", 1000);
			return;
		}
		
		//set footpon data
		setView(footpon);
		bindStepService();
		
		if(own_coupon) {
			if(!footpon.getUsed()){
				showUseButton();
				showBarcodeView(footpon);
				Log.e("log_tag", "show button: ");
			}
			else{
				use.setVisibility(View.GONE);
				showBarcodeView(footpon);
			}
		}
		else {
			if(stepsEnough(_steps, footpon)){
				showRedeemButton();
			}else{
				showNotEnoughText();
			}
		}
		
		use.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				if(footpon == null) return;
				
				if(isChecked){
					barcodeView.setVisibility(View.VISIBLE);
					File sdcard = Environment.getExternalStorageDirectory();
					File file = new File(sdcard, "user.txt");
					
					//TODO: change this save to coupon.txt
					//save(sdcard,file);
					stepService.redeemSteps(footpon.getStepsRequired());
					//check footpon used at serverside
					service.invalidate(username, footpon.getID());
					showBarcodeView(footpon);
					use.setClickable(false);
				}else{
					barcodeView.setVisibility(View.INVISIBLE);
					stepService.addSteps(footpon.getStepsRequired());
				}
			}
		});
	}

	private void showBarcodeView(Footpon fp) {
		barcodeView.setVisibility(View.VISIBLE);
		code.setVisibility(View.VISIBLE);
		barcodeView.loadUrl(
				"http://pdc-amd01.poly.edu/~jli15/footpon/barcode.php?upc=" +
				fp.getCode()
		);
	}
	
	private void showUseButton() {
		use.setText("Use it now");
	//	code.setVisibility(View.VISIBLE);
	}
	
	private void showNotEnoughText() {
		use.setText("Not Enough Steps");
		use.setEnabled(false);
	}

	private void showRedeemButton() {
		use.setText("Redeem");
		code.setVisibility(View.INVISIBLE);
	}

	private boolean stepsEnough(long point,Footpon footpon) {
		return point >= footpon.getStepsRequired();
	}

	private void getView(){
		storeName = (TextView) findViewById(R.id.detail_store_name);
		description = (TextView) findViewById(R.id.detail_description);
		code = (TextView) findViewById(R.id.code);
		expireDate = (TextView) findViewById(R.id.detail_expire_date);
		logo = (ImageView) findViewById(R.id.logo);
		stepView = (TextView) findViewById(R.id.detail_steps);
		barcodeView = (WebView) findViewById(R.id.barcode_view);
		use = (ToggleButton) findViewById(R.id.use);
		stepsRequired = (TextView) findViewById(R.id.detail_steps_required);
	}
	
	private void setView(Footpon footpon) {
		if(footpon != null){
			storeName.setText(footpon.getStoreName());
			description.setText(footpon.getRealDescription());
			expireDate.setText(footpon.getEndDate());
			logo.setImageDrawable(IconHelper.getLogo(footpon.getStoreName(), this));
			stepView.setText(String.valueOf(StepService.steps));
			stepsRequired.setText(String.valueOf(footpon.getStepsRequired()));
			code.setText(String.valueOf(footpon.getCode()));
		}
	}
	
	@Override
	public void bindStepService() {
		Log.d(SENSOR_SERVICE, "Start binding service...");
		//use getApplicationContext().bindService when bindService in Tab
		//see http://code.google.com/p/android/issues/detail?id=2483
		boolean connected = getApplicationContext().bindService(new Intent(this, 
                StepService.class), connection, Context.BIND_AUTO_CREATE);
		if(!connected){
			Toast.makeText(this, "connected service failed", 1000);
		}
    }
	
	//unbind step service, called when application stop
	@Override
	public void unbindStepService() {
		getApplicationContext().unbindService(connection);
    }
	
	//the connection that access the step service
	private ServiceConnection connection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            //IMPORTANT: get service instance:
        	stepService = ((StepService.StepBinder) service).getService();
            stepService.setStepDisplayer(FootponDetailsActivity.this);
        }

        public void onServiceDisconnected(ComponentName className) {
        	stepService = null;
        }
    };
    
	@Override
	public void passValue(long steps, long currentSteps)
	{
		stepView.setText(String.valueOf(steps));
	}
	
	@Override
    public void onPause() 
    {
		super.onStop();
    }
	
	@Override
    public void onStop() 
    {
    	super.onStop();
    	
		unbindStepService();
		// Stop receiving location notifications.
		Log.i("Footpon", "stopping the listener");
    }
	
	private void save(File sdcard, File file) {
		try {
			if (sdcard.canWrite()) {
				FileWriter writer = new FileWriter(file, true);
				BufferedWriter out = new BufferedWriter(writer);

				out.append(Long.toString(footpon.getID()));
				out.append("\n\n");
				out.close();
			}

		} catch (FileNotFoundException e) {
			Log.e("log_tag", "File not found.\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
