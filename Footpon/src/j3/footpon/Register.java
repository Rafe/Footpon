package j3.footpon;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity {

	private EditText view_userName;
	private EditText view_password;
	private EditText view_passwordConfirm;
	private Button view_submit;
	private Button view_clearAll;
	protected TextView view_firstName;
	protected TextView view_lastName;
	
	private final String SHARE_LOGIN_TAG = "LOGIN_TAG";
	private String SHARE_USERNAME = "FOOTPON_USERNAME";
	private String SHARE_PASSWORD = "FOOTPON_PASSWORD";
	private String SHARE_FIRSTNAME = "FOOTPON_FIRSTNAME";

	private ProgressDialog proDialog;
	private boolean isNetError;
	
	Handler registerHandler = new Handler() 
	{
		public void handleMessage(Message msg) {
			isNetError = msg.getData().getBoolean("isNetError");
			if (proDialog != null) {
				proDialog.dismiss();
			}
			if (isNetError) {
				Toast.makeText(Register.this, "Please check your network connection!",
						Toast.LENGTH_SHORT).show();
			}
			else {
				Toast.makeText(Register.this, "Username not available or password!",
						Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		findViews();
		setListener();
	}

	private void findViews() {
		view_userName = (EditText) findViewById(R.id.registerUserName);
		view_firstName = (EditText) findViewById(R.id.firstName);
		view_lastName = (EditText) findViewById(R.id.lastName);
		view_password = (EditText) findViewById(R.id.registerPassword);
		view_passwordConfirm = (EditText) findViewById(R.id.registerPasswordConfirm);
		view_submit = (Button) findViewById(R.id.registerSubmit);
		view_clearAll = (Button) findViewById(R.id.registerClear);
	}

	private void setListener() {
		view_submit.setOnClickListener(submitListener);
		view_clearAll.setOnClickListener(clearListener);
	}

	private OnClickListener submitListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			String userName = view_userName.getText().toString();
			String firstName = view_firstName.getText().toString();
			String lastName = view_lastName.getText().toString();
			String password = view_password.getText().toString();
			String passwordConfirm = view_passwordConfirm.getText().toString();
			boolean proceed=validateForm(userName, password, passwordConfirm);
			
			if(proceed==true)
			{
				//Code modified from http://www.helloandroid.com/tutorials/connecting-mysql-database.
				String result = "";
				InputStream is = null;

				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
					
				nameValuePairs.add(new BasicNameValuePair("username", userName));
				nameValuePairs.add(new BasicNameValuePair("firstName", firstName));
				nameValuePairs.add(new BasicNameValuePair("lastName", lastName));
				nameValuePairs.add(new BasicNameValuePair("password", password));
				nameValuePairs.add(new BasicNameValuePair("steps", Integer.toString(0)));

				//http post
				try
				{
					HttpClient httpclient = new DefaultHttpClient();

					HttpPost httppost = new HttpPost("http://pdc-amd01.poly.edu/~jli15/footpon/createAccount.php");
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
						
						String success=json_data.getString("success");
						
						if(success.equalsIgnoreCase("true"))
						{
							//Register success
							SharedPreferences share = getSharedPreferences(SHARE_LOGIN_TAG, 0);
							share.edit().putString(SHARE_USERNAME, userName).commit();
							share.edit().putString(SHARE_PASSWORD, password).commit();
							share.edit().putString(SHARE_FIRSTNAME, firstName).commit();
							share = null;
							
							//Intent intent = new Intent();
							//intent.setClass(Register.this, Coupon.class);
							//Bundle bundle = new Bundle();
							//bundle.putString("MAP_USERNAME", userName);
							//intent.putExtras(bundle);
							//startActivity(intent);
							//proDialog.dismiss();
							
							finish();
							//finish this
						}
						
						else
						{
							//Register failed
							Message message = new Message();
							Bundle bundle = new Bundle();
							bundle.putBoolean("isNetError ", isNetError);
							message.setData(bundle);
							registerHandler.sendMessage(message);
							
							//finish this
							clearForm();
						}
					}
				}

				catch(JSONException e)
				{
					Log.e("log_tag", "Error parsing data "+e.toString());
				}
			}
		}
	};

	private OnClickListener clearListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			clearForm();
		}
	};

	private boolean validateForm(String userName, String password, String password2) 
	{
		StringBuilder suggest = new StringBuilder();
		Log.d(this.toString(), "validate");
		
		boolean first=true;
		boolean second=true;
		boolean third=true;
		
		if (userName.length() < 1) 
		{
			suggest.append(getText(R.string.suggest_userName) + "\n");
			first=false;
		}
		
		if (password.length() < 1 || password2.length() < 1) 
		{
			suggest.append(getText(R.string.suggest_passwordNotEmpty) + "\n");
			second=false;
		}
		if (!password.equals(password2)) 
		{
			suggest.append(getText(R.string.suggest_passwordNotSame));
			third=false;
		}
		
		if (suggest.length() > 0) 
		{
			Toast.makeText(this, suggest.subSequence(0, suggest.length() - 1), Toast.LENGTH_SHORT).show();
		}
		
		if(first==true && second==true && third==true)
		{
			return true;
		}
		
		else
		{
			return false;
		}
	}

	private void clearForm() {
		view_userName.setText("");
		view_firstName.setText("");
		view_lastName.setText("");
		view_password.setText("");
		view_passwordConfirm.setText("");

		view_userName.requestFocus();
	}
}
