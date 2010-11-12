package j3.footpon;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
	
	private final String SHARE_LOGIN_TAG = "MAP_SHARE_LOGIN_TAG";
	private String SHARE_LOGIN_USERNAME = "MAP_LOGIN_USERNAME";
	private String SHARE_LOGIN_PASSWORD = "MAP_LOGIN_PASSWORD";

	private boolean isNetError;
	private ProgressDialog proDialog;
	
	private User currentUser=null;

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
		initView(false);
		
		setListener();
	}

	private void findViewsById() {
		view_userName = (EditText) findViewById(R.id.loginUserNameEdit);
		view_password = (EditText) findViewById(R.id.loginPasswordEdit);
		view_rememberMe = (CheckBox) findViewById(R.id.loginRememberMeCheckBox);
		view_loginSubmit = (Button) findViewById(R.id.loginSubmit);
		view_loginRegister = (Button) findViewById(R.id.loginRegister);
	}

	private void initView(boolean isRememberMe) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		String userName = share.getString(SHARE_LOGIN_USERNAME, "");
		String password = share.getString(SHARE_LOGIN_PASSWORD, "");
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

	private boolean validateLocalLogin(String userName, String password,
			String validateUrl) {
		boolean loginState = false;
		HttpURLConnection conn = null;
		DataInputStream dis = null;
		try {
			URL url = new URL(validateUrl);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.connect();
			dis = new DataInputStream(conn.getInputStream());
			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				Log.d(this.toString(), "getResponseCode() unsuccessful");
				isNetError = true;
				return false;
			}
			
			int loginStateInt = dis.readInt();
			if (loginStateInt > 0) {
				loginState = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			isNetError = true;
			Log.d(this.toString(), e.getMessage() + "  127 line");
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
		
		if (loginState) {
			if (isRememberMe()) {
				saveSharePreferences(true, true);
			} else {
				saveSharePreferences(true, false);
			}
		} else {
			if (!isNetError) {
				clearSharePassword();
			}
		}
		if (!view_rememberMe.isChecked()) {
			clearSharePassword();
		}
		return loginState;
	}

	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		if (saveUserName) {
			Log.d(this.toString(), "saveUserName="
					+ view_userName.getText().toString());
			share.edit().putString(SHARE_LOGIN_USERNAME,
					view_userName.getText().toString()).commit();
		}
		if (savePassword) {
			share.edit().putString(SHARE_LOGIN_PASSWORD,
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
	
	private OnClickListener registerLstener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(Login.this, Register.class);
			startActivity(intent);
		}
	};

	private void setListener() {
		view_loginSubmit.setOnClickListener(submitListener);
		view_loginRegister.setOnClickListener(registerLstener);
		view_rememberMe.setOnCheckedChangeListener(rememberMeListener);
	}

	private void clearSharePassword() {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
		share.edit().putString(SHARE_LOGIN_PASSWORD, "").commit();
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
			String username="";
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

			catch(Exception ee)
			{
				Log.e("log_tag", "Error in http connection "+ee.toString());
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
			}

			catch(Exception eee)
			{
				Log.e("log_tag", "Error converting result "+eee.toString());
			}

			//parse json data
			try
			{
				JSONArray jArray = new JSONArray(result);
					
				for(int i=0;i<jArray.length();i++)
				{
					JSONObject json_data = jArray.getJSONObject(i);
					
					currentUser=new User(json_data.getString("username"), json_data.getString("firstName"), json_data.getString("lastName"), json_data.getLong("points"));
					
					//Log.i("log_tag","Longitude: "+json_data.getDouble("longitude")+", Latitude: "+json_data.getDouble("latitude"));
					//int longitude=(int) (json_data.getDouble("longitude")*1000000);
					//int latitude=(int) (json_data.getDouble("latitude")*1000000);
					//String storeName=json_data.getString("storeName");
					//String hiddenDescription=json_data.getString("hiddenDescription");
					//String realDescription=json_data.getString("realDescription");
					//int pointsRequired =(int) json_data.getInt("pointsRequired");
						
				    //GeoPoint point = new GeoPoint(latitude, longitude);
				    //OverlayItem overlayitem = new OverlayItem(point, storeName, hiddenDescription+".\nPoints Required:"+pointsRequired);
					
				    //itemizedoverlay.addOverlay(overlayitem);
				    //mapOverlays.add(itemizedoverlay);
				}
			}

			catch(JSONException e)
			{
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
		
			if(currentUser!=null)
			{
				// login success
				Intent intent = new Intent();
				intent.setClass(Login.this, Coupon.class);
				Bundle bundle = new Bundle();
				bundle.putString("MAP_USERNAME", userName);
				intent.putExtras(bundle);
				startActivity(intent);
				proDialog.dismiss();
			} 
			
			else 
			{
				// login failed
				Message message = new Message();
				Bundle bundle = new Bundle();
				bundle.putBoolean("isNetError ", isNetError);
				message.setData(bundle);
				loginHandler.sendMessage(message);
			}
		}
	}
}
