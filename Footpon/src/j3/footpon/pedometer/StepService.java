package j3.footpon.pedometer;

import j3.footpon.FootponMapActivity;
import j3.footpon.R;
import j3.footpon.model.User;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.widget.Toast;

public class StepService extends Service implements StepListener{
	
	public static long steps;
	public static long currentSteps;
	
	boolean isStarted = false;
	
	private SharedPreferences state;
	
	StepDetector stepDetector;
	SensorManager sensorManager;
	
	StepDisplayer stepDisplayer;
	StepBinder binder = new StepBinder();
	
	public class StepBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }
	
	private WakeLock wakeLock;
	private NotificationManager notificationManager;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}
	
	@Override
    public void onCreate() {
        super.onCreate();
        
        //wake power service
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "StepService");
        wakeLock.acquire();
        
        state = getSharedPreferences(User.SHARE_USER_INF_TAG, 0);
        currentSteps = 0;
        steps = state.getLong(User.SHARE_POINTS, 0);
        
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        showNotification();
        
        Log.d(SENSOR_SERVICE, "Created StepService...");
        stepDetector = new StepDetector();
        stepDetector.addStepListener(this);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(SensorManager.SENSOR_ACCELEROMETER);
        sensorManager.registerListener(stepDetector,sensor,SensorManager.SENSOR_DELAY_FASTEST);
        
	}	
	
	public void registerListener(StepListener listener){
		if(stepDetector != null){
			stepDetector.addStepListener(listener);
		}
	}
	
	@Override
	public void onDestroy(){
		sensorManager.unregisterListener(stepDetector);
		
		wakeLock.release();
		
		savePoints();
		
		notificationManager.cancel(R.string.app_name);
	}
	
	@Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if(!isStarted){
        	// Tell the user we started.
        	Toast.makeText(this, "Started pedometer", Toast.LENGTH_SHORT).show();
        	isStarted = true;
        }
    }
	
	/**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        CharSequence text = getText(R.string.app_name);
        Notification notification = new Notification(R.drawable.icon, null,
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_NO_CLEAR | Notification.FLAG_ONGOING_EVENT;
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, FootponMapActivity.class), 0);
        notification.setLatestEventInfo(this, text,
                getText(R.string.app_name), contentIntent);
        notificationManager.notify(R.string.app_name, notification);
    }
    
    public void setStepDisplayer(StepDisplayer displayer){
    	stepDisplayer = displayer;
    	displayer.passValue(steps, currentSteps);
    }
    
	@Override
	public void onStep() {

		currentSteps+=1;
		steps += 1;
		refresh();
	}

	public boolean redeemSteps(long s) {
		
		if(steps < s){
			return false;
		}
		steps -= s;
		refresh();
		savePoints();
		return true;
	}

	private void refresh() {
		if(stepDisplayer != null){
			stepDisplayer.passValue(steps, currentSteps);
		}
	}

	public boolean addSteps(long s) {
		steps += s;
		refresh();
		savePoints();
		return true;
	}
	
	private boolean savePoints(){
		SharedPreferences.Editor stateEditor;
		stateEditor = state.edit();
	    stateEditor.putLong(User.SHARE_POINTS, steps);
	    stateEditor.commit();
	    return true;
	}

}
