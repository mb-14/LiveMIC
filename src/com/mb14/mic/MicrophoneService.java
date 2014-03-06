package com.mb14.mic;

import com.mb14.mic.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import opensl.opensl;

public class MicrophoneService extends Service{
	
	private static final String APP_TAG = "LiveMIC"; 
	private NotificationManager mNotificationManager;
	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Override
    public void onCreate() {   	
    	// notification service
    	mNotificationManager  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);  
        record();
		showNotification();
    }
  

    @Override
    public void onDestroy() {    	    	
    	opensl.stop_process();
    	mNotificationManager.cancel(0);
    	mNotificationManager = null;
		super.onDestroy();
    }
    
	
    
	
	
	public void record() {
		Thread t = new Thread() {
			public void run() {		
				 setPriority(Thread.MAX_PRIORITY);
				opensl.start_process();

			}
		};
		
		t.start();
		
	}
	@SuppressWarnings("deprecation")
	private void showNotification(){
		String text = getString(R.string.app_name);
		Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
		Intent startIntent = new Intent(this,MicrophoneActivity.class);
		startIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(this, 0, startIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
		notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
		notification.setLatestEventInfo(this, 
				getString(R.string.app_name), 
				getString(R.string.mic_active), 
				intent);
		mNotificationManager.notify(0, notification);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);

	}
}
