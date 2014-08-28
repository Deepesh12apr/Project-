package com.myapp.screenlocktest;






import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	private static final int ADMIN_INTENT = 15;
	private static final String description = "Some Description About Your Admin";
	private DevicePolicyManager mDevicePolicyManager; 
	private ComponentName mComponentName;  
	Intent s = new Intent();
	private AdView adView;
	 
	public  void fun(){
		PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
	    final WakeLock wakeLock = pm.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "TAG");
	    wakeLock.release();
	    
		}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myscreen);
        
        mDevicePolicyManager = (DevicePolicyManager)getSystemService(  
				Context.DEVICE_POLICY_SERVICE);  
		mComponentName = new ComponentName(this, MyAdminReceiver.class);  
		Button btnEnableAdmin = (Button) findViewById(R.id.btnEnable);
		Button btnDisableAdmin = (Button) findViewById(R.id.btnDisable);
		Button btnLock = (Button) findViewById(R.id.btnLock);
		TextView title =(TextView) findViewById(R.id.title);

		btnEnableAdmin.setOnClickListener(this);
		btnDisableAdmin.setOnClickListener(this);
		btnLock.setOnClickListener(this);
		
		 // Create an ad.
	    adView = new AdView(this);
	    adView.setAdSize(AdSize.BANNER);
	    adView.setAdUnitId("ca-app-pub-8549203901946162/6217091139");
		
	    LinearLayout layout = (LinearLayout) findViewById(R.id.add);
	    layout.addView(adView);
		
		 Typeface custom_font = Typeface.createFromAsset(getAssets(),
	        		"fonts/candy.ttf");
	      title.setTypeface(custom_font);
		
	      AdRequest adRequest = new AdRequest.Builder()
	        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	        .addTestDevice("INSERT_YOUR_HASHED_DEVICE_ID_HERE")
	        .build();

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	      // Look up the AdView as a resource and load a request.
	     // AdView adView = (AdView) this.findViewById(R.id.adView);
	     // AdRequest adRequest = new AdRequest.Builder().build();
	      //adView.loadAd(adRequest);
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnEnable:
			Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
			intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mComponentName);
			intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,description);
			startActivityForResult(intent, ADMIN_INTENT);
			
			//Toast.makeText(getApplicationContext(), "going to service", Toast.LENGTH_SHORT).show();
			boolean ischecked = mDevicePolicyManager.isAdminActive(mComponentName);  
			 
				//Intent s = new Intent();
				s.setAction("com.myapp.screenlocktest.MyService");
				//s.setClass(this, MyService.class);
				//Toast.makeText(getApplicationContext(), "service", Toast.LENGTH_SHORT).show();
				startService(s);
				Toast.makeText(getApplicationContext(), "Starting lock services", Toast.LENGTH_SHORT).show();
				
				Intent second = new Intent(this, lightonservice.class);
				startService(second);
			
			
			
			break;

		case R.id.btnDisable:
		//	s.setAction("com.myapp.screenlocktest.MyService");
			Intent ser = new Intent(this,MyService.class);
			System.out.println("killing myservice");
			stopService(ser);
			Intent sec = new Intent(this,lightonservice.class);
			stopService(sec);
			mDevicePolicyManager.removeActiveAdmin(mComponentName);  
			Toast.makeText(getApplicationContext(), "Admin registration removed", Toast.LENGTH_SHORT).show();
			
			//Intent act = new Intent(this,MainActivity.class);
			finish();
			break;

		case R.id.btnLock:
			boolean isAdmin = mDevicePolicyManager.isAdminActive(mComponentName);  
			if (isAdmin) {  
				mDevicePolicyManager.lockNow();  
			}else{
				Toast.makeText(getApplicationContext(), "Not Registered as admin", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADMIN_INTENT) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(getApplicationContext(), "Registered As Admin", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "Failed to register as Admin", Toast.LENGTH_SHORT).show();
			}
		}
	}
   
	 @Override
	  public void onStart() {
	    super.onStart();
	     // The rest of your onStart() code.
	    EasyTracker.getInstance().activityStart(this);  // Add this method.
	  }

	  @Override
	  public void onStop() {
	    super.onStop();
	     // The rest of your onStop() code.
	    EasyTracker.getInstance().activityStop(this);  // Add this method.
	  }
	  @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 if (adView != null) {
		      adView.resume();
		    }
	}
	  @Override
	  public void onPause() {
	    if (adView != null) {
	      adView.pause();
	    }
	    super.onPause();
	  }
	  @Override
	  public void onDestroy() {
	    // Destroy the AdView.
	    if (adView != null) {
	      adView.destroy();
	    }
	    super.onDestroy();
	  }
}
