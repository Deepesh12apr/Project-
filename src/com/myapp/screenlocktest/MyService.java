package com.myapp.screenlocktest;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.Toast;

public class MyService extends Service implements SensorEventListener  {

	private static final int ADMIN_INTENT = 15;
	private static final String description = "Some Description About Your Admin";
	private DevicePolicyManager mDevicePolicyManager;
	private ComponentName mComponentName;
	public int lockflag = 0;
	private SensorManager mSensorManager;
    public Sensor mSensor;
    private PowerManager.WakeLock wl;
   public boolean incoming;
 
 
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		System.out.println("Myservice started");
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY); 
        
    	mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
    	
    	mDevicePolicyManager = (DevicePolicyManager)getSystemService(  
				Context.DEVICE_POLICY_SERVICE);  
		mComponentName = new ComponentName(this, MyService.class);
    	
    	Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
		//startActivityForResult(intent, ADMIN_INTENT);
		System.out.println("start service success 0 0000");
		startService(intent);
		System.out.println("start service success");
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}
     
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		//PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
	    //final WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
        //wakeLock.acquire();
		System.out.println(event.values[0]);
		 boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName); 
		if (event.values[0] == 0 && lockflag == 0) {

			//boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);  
			
				mDevicePolicyManager.lockNow();
                lockflag=1;
          
		}
		if(event.values[0] == 1 && lockflag == 1)
		{
			
			PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
		    final WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
		    wakeLock.acquire();
		    System.out.println("lock accquired");
	        lockflag=0;
	        
	        new Thread() {
				public void run() {
					try {
						sleep(3000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					wakeLock.release();
					System.out.println("lock released");
				}
			}.start();
		}
		if(event.values[0] == 0 && lockflag == 1)
		{
			
			mDevicePolicyManager.lockNow();
			System.out.println("locked again");
			
		}
		
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Intent sss =new Intent(getBaseContext(), MyService.class);
		super.onConfigurationChanged(newConfig);
		  // Checks the orientation of the screen for landscape and portrait and set portrait mode always
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // stopSelf();
        	lockflag=2;
        	//Toast.makeText(getBaseContext(), "landscape", Toast.LENGTH_SHORT).show();
        	//Intent sss =new Intent(getBaseContext(), MyService.class);
        	System.out.println("idle");
        	stopService(sss);
        } 
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        	startService(sss);
        	System.out.println("idle");
        	lockflag=1;
        	//Toast.makeText(getBaseContext(), "potrait", Toast.LENGTH_SHORT).show();
        }
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("Myservice died");
		super.onDestroy();
	
	}
}
