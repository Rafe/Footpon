package j3.footpon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowInformation extends Activity {
	private final String SHARE_USER_INF_TAG = "USER_INF_TAG";
	private String SHARE_USERNAME = "FOOTPON_USERNAME";
	private String SHARE_PASSWORD = "FOOTPON_PASSWORD";
	private String SHARE_FIRSTNAME = "FOOTPON_FIRSTNAME";
	private String SHARE_LASTNAME = "FOOTPON_LASTNAME";
	private String SHARE_POINTS = "FOOTPON_POINTS";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_information);
		
		SharedPreferences share = getSharedPreferences(SHARE_USER_INF_TAG, 0);
		
		String username = share.getString(SHARE_USERNAME, "");
		if(!username.equals(""))
		{
			String firstName = share.getString(SHARE_FIRSTNAME, "");
			String lastName = share.getString(SHARE_LASTNAME, "");
			long points = share.getLong(SHARE_POINTS, 0);
			
			StringBuilder text=new StringBuilder();
			text.append("Welcome, " + firstName + " " + lastName);
			text.append('\n');
			text.append('\n');
			text.append("Your username: ");
			text.append(username);
			text.append("\nYour stored points: ");
			text.append(points);
			text.append('\n');
			
			text.append("\nCoupon ID:\n");
			
			File sdcard=Environment.getExternalStorageDirectory();
			File file=new File(sdcard, "coupons.txt");
			
			try
			{
				BufferedReader reader=new BufferedReader(new FileReader(file));
				String line;
				
				while((line=reader.readLine())!= null)
				{
					text.append(line);
					text.append('\n');
				}
			}
			catch (FileNotFoundException e)
			{
				Log.e("log_tag", "File not found. "+e.toString());
			}
			catch (IOException e) 
			{
				e.printStackTrace();
			}
			
			TextView view=(TextView)findViewById(R.id.userText);
			view.setText(text);
			
			Button go=(Button) findViewById(R.id.mapButton);
			go.setOnClickListener(map);
			
			Button stop=(Button) findViewById(R.id.logoutButton);
			stop.setOnClickListener(logout);
		}
		
		else
		{
			startActivity(new Intent(ShowInformation.this, Login.class));
		}
	}
	
	private View.OnClickListener map=new View.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			Intent intent=new Intent();
			intent.setClass(ShowInformation.this, MainActivity.class);
			startActivity(intent);
	//		finish();
		}
	};
	
	private View.OnClickListener logout=new View.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			SharedPreferences share = getSharedPreferences(SHARE_USER_INF_TAG, 0);
			share.edit().putString(SHARE_USERNAME, "").commit();
			share.edit().putString(SHARE_PASSWORD, "").commit();
			share.edit().putString(SHARE_FIRSTNAME, "").commit();
			share.edit().putString(SHARE_LASTNAME, "").commit();
			share.edit().putLong(SHARE_POINTS, 0).commit();
			share = null;
			
			File sdcard=Environment.getExternalStorageDirectory();
			File file=new File(sdcard, "coupons.txt");
			file.delete();
			
			Intent intent=new Intent();
			intent.setClass(ShowInformation.this, MainActivity.class);
			startActivity(intent);
		}
	};
}
