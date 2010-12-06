package j3.footpon;

import j3.footpon.pedometer.StepService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CaloriesBurned extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calories_burned);

		Long currentSteps=StepService.currentSteps;
		Double caloriesBurned=currentSteps*.05;
			
		StringBuilder text=new StringBuilder();
		text.append("Current steps since pedometer started: ");
		text.append(currentSteps);
		text.append("\nCalories burned: ");
		text.append(caloriesBurned);
			
		TextView view=(TextView)findViewById(R.id.caloriesBurnedText);
		view.setText(text);
			
		Button go=(Button) findViewById(R.id.doneButton);
		go.setOnClickListener(done);
	}
	
	private View.OnClickListener done=new View.OnClickListener()
	{
		@Override
		public void onClick(View v) 
		{
			Intent intent=new Intent();
			intent.setClass(CaloriesBurned.this, MainActivity.class);
			startActivity(intent);
		}
	};
}
