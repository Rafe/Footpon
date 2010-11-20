package j3.footpon;

import j3.footpon.R;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ShowInformation extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		//Reading from file code modified from http://stackoverflow.com/questions/2902689/read-text-file-data-in-android
		File sdcard=Environment.getExternalStorageDirectory();
		File file=new File(sdcard, "user.txt");

		if(file.exists())
		{
			setContentView(R.layout.user_information);
			
			StringBuilder text=new StringBuilder();
			
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
				text.append("No information found.");
				text.append('\n');
			} 
			
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
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
			startActivity(
    	    	new Intent(ShowInformation.this, Login.class));
		}
	}
	
	private View.OnClickListener map=new View.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			Intent intent=new Intent();
			intent.setClass(ShowInformation.this, FootponMapActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	private View.OnClickListener logout=new View.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			File sdcard=Environment.getExternalStorageDirectory();
			File file=new File(sdcard, "user.txt");
			file.delete();
			
			Intent intent=new Intent();
			intent.setClass(ShowInformation.this, FootponMapActivity.class);
			startActivity(intent);
		}
	};
}
