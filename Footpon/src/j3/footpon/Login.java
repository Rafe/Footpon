package j3.footpon;

import j3.footpon.model.User;
import j3.footpon.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
	
	private final String SHARE_LOGIN_TAG = "LOGIN_TAG";
	private String SHARE_USERNAME = "FOOTPON_USERNAME";
	private String SHARE_PASSWORD = "FOOTPON_PASSWORD";
	private String SHARE_FIRSTNAME = "FOOTPON_FIRSTNAME";

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

	/*private boolean validateLocalLogin(String userName, String password,
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
	}*/

	private void saveSharePreferences(boolean saveUserName, boolean savePassword) {
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
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
		SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
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
		//	String username="";
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
				Log.e("log_tag", "result: "+result);
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
				
				String username=json_data.getString("username");
				String firstName=json_data.getString("firstName");
				String lastName=json_data.getString("lastName");
				Long points=json_data.getLong("points");

	    		//Writing to file code modified from http://groups.google.com/group/android-beginners/browse_thread/thread/b8fd909e33eab7c1
    			File root=Environment.getExternalStorageDirectory();
    			
    			if(root.canWrite())
    			{
    				File file=new File(root, "user.txt");
    				FileWriter writer=new FileWriter(file, true);
    				BufferedWriter out=new BufferedWriter(writer);
    				
    				out.write("Username: ");
    				out.write(username);
    				out.write("\n");
    				out.write("First Name: ");
    				out.write(firstName);
    				out.write("\n");
    				out.write("Last Name: ");
    				out.write(lastName);
    				out.write("\n");
    				out.write("Previously Stored Points: ");
    				out.write(Long.toString(points));
    				out.write("\n\n");
    				out.write("Coupons ID Redeemed:");
    				out.write("\n");
    				out.close();
    			}
				
				for(i=0;i<jArray.length();i++)
				{
					json_data = jArray.getJSONObject(i);
					
					//String username=json_data.getString("username");
					//String firstName=json_data.getString("firstName");
					//String lastName=json_data.getString("lastName");
					//Long points=json_data.getLong("points");
					Long id=json_data.getLong("id");
					
					//currentUser=new User(username, firstName, lastName, points);
					
		    		try 
					{
///		    			File root=Environment.getExternalStorageDirectory();
		    			
		    			if(root.canWrite())
		    			{
		    				File file=new File(root, "user.txt");
		    				FileWriter writer=new FileWriter(file, true);
		    				BufferedWriter out=new BufferedWriter(writer);
		    				
		    				//out.write("Username: ");
		    				//out.write(username);
		    				//out.write("\n");
		    				//out.write("First Name: ");
		    				//out.write(firstName);
		    				//out.write("\n");
		    				//out.write("Last Name: ");
		    				//out.write(lastName);
		    				//out.write("\n");
		    				//out.write("Previously Stored Points: ");
		    				out.write(Long.toString(id));
		    				out.write("\n");
		    				out.close();
		    			}
					}
					
					catch (IOException e) 
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			catch(JSONException e)
			{
				Log.e("log_tag", "Error parsing data "+e.toString());
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			File sdcard=Environment.getExternalStorageDirectory();
			File file=new File(sdcard, "user.txt");

			if(file.exists())
			{
				// login success
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
