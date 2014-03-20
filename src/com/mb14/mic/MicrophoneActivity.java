package com.mb14.mic;

import com.mb14.mic.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class
        MicrophoneActivity extends Activity{
	
	private static final String APP_TAG         = "Microphone";
	private static final String IS_SERVICE_STARTED = "isServiceStarted";
	ToggleButton toggle;
	SharedPreferences mSharedPreferences;
	boolean           mActive = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        toggle = (ToggleButton) findViewById(R.id.toggle_service);  
    	mActive = isServiceStarted(this);
    	if (mActive){
    		toggle.setChecked(true);
    	}
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !mActive) {
                	startService(new Intent(MicrophoneActivity.this, MicrophoneService.class));
                	setServiceStarted(MicrophoneActivity.this, true);
                } else {
                	stopService(new Intent(MicrophoneActivity.this, MicrophoneService.class));
                	setServiceStarted(MicrophoneActivity.this, false);
                	
                }
            }
        });
        
    	
       
    	
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	Log.d(APP_TAG, "Closing mic activity");

    }
  
	public static boolean isServiceStarted(Context context)
	{
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		return pref.getBoolean(IS_SERVICE_STARTED, false);
	}

	public static void setServiceStarted(Context context, boolean isStarted){
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(IS_SERVICE_STARTED, isStarted);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = new Intent(MicrophoneActivity.this,AboutActivity.class);
		startActivity(i);
		return true;
	}
  
}
