package com.myapp.screenlocktest;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.sax.StartElementListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class Myreceiver extends BroadcastReceiver {

	private final String BOOT_ACTION = "android.intent.action.BOOT_COMPLETED";
   
	boolean incoming;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
		Intent is = new Intent(context, MyService.class);
		
		String action = intent.getAction();
		/*if(action.equals())
		{
			context.stopService(is);
		}*/
		if (action.equalsIgnoreCase(BOOT_ACTION)) {
                        //check for boot complete event & start your service
			Toast.makeText(context, "phone restarted", Toast.LENGTH_SHORT).show();
			context.startService(is);
			Toast.makeText(context, "service restarted", Toast.LENGTH_SHORT).show();
		} 
		
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_RINGING)) 
		{
			
			Toast.makeText(context, "Incoming calll caught", Toast.LENGTH_SHORT).show();
			
			context.stopService(is);
			
			Toast.makeText(context, "service ends", Toast.LENGTH_SHORT).show();
		/*	incoming =true;
			Intent i = new Intent(context, lightonservice.class);
		        i.putExtra("incoming_call", incoming);
		        context.startService(i);
		        Toast.makeText(context, "Incoming calll after service", Toast.LENGTH_SHORT).show();*/
		}
		
	}
   
}
