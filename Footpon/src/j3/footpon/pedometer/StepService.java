package j3.footpon.pedometer;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class StepService extends Service{
	
	StepDetector stepDetector;
	SensorManager sensorManager;
	StepBinder binder = new StepBinder();
	
	public class StepBinder extends Binder {
        public StepService getService() {
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
        Log.d(SENSOR_SERVICE, "Created StepService...");
        stepDetector = new StepDetector();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(sensorManager.SENSOR_ACCELEROMETER);
        sensorManager.registerListener(stepDetector,sensor,sensorManager.SENSOR_DELAY_FASTEST);
      
	}
	
	public void registerListener(StepListener listener){
		if(stepDetector != null){
			stepDetector.addStepListener(listener);
		}
	}
	
	@Override
	public void onDestroy(){
		sensorManager.unregisterListener(stepDetector);
	}
	
	@Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        // Tell the user we started.
        Toast.makeText(this, "Started pedometer", Toast.LENGTH_SHORT).show();
    }

}
