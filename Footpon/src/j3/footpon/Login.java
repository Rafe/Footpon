package j3.footpon;

import j3.footpon.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class Login extends Activity {

	private String userName;
	private String password;
	private EditText view_userName;
	private EditText view_password;
	private CheckBox view_rememberMe;
	private Button view_loginSubmit;
	private Button view_loginRegister;
	
	private final String SHARE_REMEBER_ME_TAG = "REMEBER_ME_TAG";
	private final String SHARE_USER_INF_TAG = "USER_INF_TAG";
	private String SHARE_USERNAME = "FOOTPON_USERNAME";
	private String SHARE_PASSWORD = "FOOTPON_PASSWORD";
	private String SHARE_FIRSTNAME = "FOOTPON_FIRSTNAME";
	private String SHARE_LASTNAME = "FOOTPON_LASTNAME";
	private String SHARE_STEPS = "FOOTPON_POINTS";

	private boolean isNetError;
	private ProgressDialog proDialog;
	
	//handle login event and response, implement "handleMessage(msg)"
	Handler loginHandler = new Handler() {
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(Login.this, "Please check your network connection!",
						Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(Login.this, "Wrong username or password!",
						Toast.LENGTH_SHORT).show();
				
				clearSharePassword();
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		findViewsById();
		initView();
		setListener();
	}

	private void findViewsById() {
		view_userName = (EditText) findViewById(R.id.loginUserNameEdit);
		view_password = (EditText) findViewById(R.id.loginPasswordEdit);
		view_rememberMe = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);
		view_loginSubmit = (Button) findViewById(R.id.loginSubmit);
		view_loginRegister = (Button) findViewById(R.id.loginRegister);
	}

	private void initView() {
		SharedPreferences share = getSharedPreferences(SHARE_REMEBER_ME_TAG, 0);
		String userName = share.getString(SHARE_USERNAME, "");
		String password = share.getString(SHARE_PASSWORD, "");
		Log.d(this.toString(), "userName=" + userName + " password=" + password);
		if (!"".equals(userName)) {
			view_userName.setText(userName);
		}
		if (!"".equals(password)) {
			view_password.setText(password);
			view_rememberMe.setChecked(true);
		}

		share = null;
	}
	
	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_REMEBER_ME_TAG, 0);
		if (saveUserName) {
			Log.d(this.toString(), "saveUserName="
					+ view_userName.getText().toString());
			share.edit().putString(SHARE_USERNAME,
					view_userName.getText().toString()).commit();
		}
		if (savePassword) {
			share.edit().putString(SHARE_PASSWORD,
					view_password.getText().toString()).commit();
		}
		share = null;
	}

	private boolean isRememberMe() {
		if (view_rememberMe.isChecked()) {
			return true;
		}
		return false;
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			proDialog = ProgressDialog.show(Login.this, "Connecting...",
					"One second...", true, true);
			
			Thread loginThread = new Thread(new LoginFailureHandler());
			loginThread.start();
		}
	};

	private OnCheckedChangeListener rememberMeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
		}
	};
	
	private OnClickListener registerListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Login.this, Register.class);
			startActivity(intent);
		}
	};

	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_loginRegister.setOnClickListener(registerListener);
		view_rememberMe.setOnCheckedChangeListener(rememberMeListener);
	}

	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_REMEBER_ME_TAG, 0);
		share.edit().putString(SHARE_PASSWORD, "").commit();
		share = null;
	}

	class LoginFailureHandler implements Runnable 
	{
		@Override
		public void run() 
		{
			userName = view_userName.getText().toString();
			password = view_password.getText().toString();

			//Code modified from http://www.helloandroid.com/tutorials/connecting-mysql-database.
			String result = "";
			InputStream is = null;

			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				
			nameValuePairs.add(new BasicNameValuePair("username", userName));
			nameValuePairs.add(new BasicNameValuePair("password", password));

			//http post
			try
			{
				HttpClient httpclient = new DefaultHttpClient();

				HttpPost httppost = new HttpPost("http://pdc-amd01.poly.edu/~jli15/footpon/getUser.php");
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
			//	Log.e("log_tag", "result: "+result);
			}
			catch(Exception e)
			{
				Log.e("log_tag", "Error converting result "+e.toString());
			}

			if(result.equals("null\n"))
			{
				//// login failed
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError ", isNetError);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
			else
			{
				//// login success
				//parse json data
				try
				{
					JSONArray jArray = new JSONArray(result);

					int i=0;
					JSONObject json_data = jArray.getJSONObject(i);
					
					String _userName=json_data.getString("username");
					String _password=json_data.getString("password");
					String _firstName=json_data.getString("firstName");
					String _lastName=json_data.getString("lastName");
					Long _steps=json_data.getLong("steps");
					
					SharedPreferences share = getSharedPreferences(SHARE_USER_INF_TAG, 2);
					share.edit().putString(SHARE_USERNAME, _userName).commit();
					share.edit().putString(SHARE_PASSWORD, _password).commit();
					share.edit().putString(SHARE_FIRSTNAME, _firstName).commit();
					share.edit().putString(SHARE_LASTNAME, _lastName).commit();
					
				//	share.edit().putLong(SHARE_STEPS, _steps).commit();
					try 
					{
						File root=Environment.getExternalStorageDirectory();
		    			
		    			if(root.canWrite())
		    			{
		    				File file=new File(root, "steps.txt");
		    				FileWriter writer=new FileWriter(file, true);
		    				BufferedWriter out=new BufferedWriter(writer);
		    				Log.e("log_tag", "result: "+_steps);
		    				out.write(Long.toString(_steps));
		    				out.write("\n");
		    				out.close();
		    			}
					}
	    			catch (IOException e) 
					{
						e.printStackTrace();
					}
					
					for(i=0;i<jArray.length();i++)
					{
						json_data = jArray.getJSONObject(i);
						Long id = json_data.getLong("id");
					//	Log.e("log_tag", "ID: "+id);
						try 
						{
							File root=Environment.getExternalStorageDirectory();
			    			
			    			if(root.canWrite())
			    			{
			    				File file=new File(root, "coupons.txt");
			    				FileWriter writer=new FileWriter(file, true);
			    				BufferedWriter out=new BufferedWriter(writer);
			    				
			    				out.write(Long.toString(id));
			    				out.write("\n");
			    				out.close();
			    			}
						}
		    			catch (IOException e) 
						{
							e.printStackTrace();
						}
					}
					
					if (isRememberMe()) {
						saveSharePreferences(true, true);
					} else {
						saveSharePreferences(true, false);
					}
					if (!view_rememberMe.isChecked()) {
						clearSharePassword();
					}
					
					Intent intent = new Intent();
					intent.setClass(Login.this, ShowInformation.class);
					startActivity(intent);
					proDialog.dismiss();
				}
				catch(JSONException e)
				{
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}
		}
	}
}
