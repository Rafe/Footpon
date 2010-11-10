package j3.footpon.pedometer;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class StepService extends Service{
	
	StepDetector stepDetector;
	SensorManager sensorManager;
	StepBinder binder = new StepBinder();
	
	public class StepBinder extends Binder {
        StepService getService() {
            return StepService.this;
        }
    }
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        
        stepDetector = new StepDetector();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensorList = sensorManager.getSensorList(
        		sensorManager.SENSOR_ACCELEROMETER | 
        		SensorManager.SENSOR_MAGNETIC_FIELD | 
                SensorManager.SENSOR_ORIENTATION);
        for (Sensor s : sensorList){
        	sensorManager.registerListener(stepDetector,s,sensorManager.SENSOR_DELAY_FASTEST);
        }
        
	}
	
	public void registerListener(StepListener listener){
		if(stepDetector != null){
			stepDetector.addStepListener(listener);
		}
	}
	
	@Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // Tell the user we started.
        Toast.makeText(this, "Started pedometer", Toast.LENGTH_SHORT).show();
    }

}
