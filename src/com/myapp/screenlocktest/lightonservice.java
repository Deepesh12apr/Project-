package com.myapp.screenlocktest;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

public class lightonservice extends Service {

	BroadcastReceiver mreceiver;
	boolean incoming;
	public int lockflag;
	@Override
	public IBinder onBind(Intent arg0) {;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		Intent sss =new Intent(getBaseContext(), MyService.class);
		super.onConfigurationChanged(newConfig);
		  // Checks the orientation of the screen for landscape and portrait and set portrait mode always
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // stopSelf();
        	//lockflag=2;
        	//Toast.makeText(getBaseContext(), "landscape", Toast.LENGTH_SHORT).show();
        	//Intent sss =new Intent(getBaseContext(), MyService.class);
        	System.out.println("sec service land");
        	stopService(sss);
        } 
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
        	startService(sss);
        	System.out.println("sec service pot");
        	//lockflag=1;
        	//Toast.makeText(getBaseContext(), "potrait", Toast.LENGTH_SHORT).show();
        }
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Toast.makeText(this, "second Service active", Toast.LENGTH_SHORT).show();
		
        
       
	}
}
