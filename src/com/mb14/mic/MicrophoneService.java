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
	
	private static final String APP_TAG = "Microphone";
	private static final int mSampleRate = 44100;
	private static final int mFormat     = AudioFormat.ENCODING_PCM_16BIT;
	
	private AudioTrack              mAudioOutput;
	private AudioRecord             mAudioInput;
	private int                     mInBufferSize;
	private int                     mOutBufferSize;
	SharedPreferences               mSharedPreferences;
 
	private NotificationManager     mNotificationManager;
	
	
	  
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
    @Override
    public void onCreate() {   	
    	// notification service
    	mNotificationManager  = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);    	// create input and output streams
      /*
    	mInBufferSize  = AudioRecord.getMinBufferSize(mSampleRate, AudioFormat.CHANNEL_IN_MONO, mFormat);
        mOutBufferSize = AudioTrack.getMinBufferSize(mSampleRate, AudioFormat.CHANNEL_OUT_MONO, mFormat);
        mAudioInput = new AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION, mSampleRate, AudioFormat.CHANNEL_IN_MONO, mFormat, mInBufferSize);
        mAudioOutput = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate, AudioFormat.CHANNEL_OUT_MONO, mFormat, mOutBufferSize, AudioTrack.MODE_STREAM);
        
        */
        record();
		showNotification();
    }
  

    @Override
    public void onDestroy() {    	    	
    	//mAudioInput.release();
    	//mAudioOutput.release();
    	opensl.stop_process();
    	mNotificationManager.cancel(0);
    	mNotificationManager = null;
		super.onDestroy();
    }
    
	
    
	
	
	public void record() {
		Thread t = new Thread() {
			public void run() {		
				android.os.Process.setThreadPriority
		         (android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
				opensl.start_process();
			//	recordLoop();

			}
			byte b[] = new byte[mInBufferSize];
			private void recordLoop() {
				if ( mAudioOutput.getState() != AudioTrack.STATE_INITIALIZED || mAudioInput.getState() != AudioTrack.STATE_INITIALIZED) {
					return;
				}
				else {
					
					try {
					
						try { mAudioOutput.play(); }          catch (Exception e) { Log.e(APP_TAG, "Failed to start playback"); return; }
						try { mAudioInput.startRecording(); } catch (Exception e) { Log.e(APP_TAG, "Failed to start recording"); mAudioOutput.stop(); return; }
						
						try {
					        
					        while(true) {
					        	mAudioInput.read(b,0, mInBufferSize);
					        	mAudioOutput.write(b, 0, b.length);
					        }
						}
						catch (Exception e) {
							Log.d(APP_TAG, "Error while recording, aborting.");
						}
			        
				        
					}
					catch (Exception e) {
						Log.d(APP_TAG, "Error somewhere in record loop.");				
					}
				}
			
		
			}
		};
		
		t.start();
		
	}
	@SuppressWarnings("deprecation")
	private void showNotification(){
		String text = getString(R.string.app_name);
		Notification notification = new Notification(R.drawable.status, text, System.currentTimeMillis());
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
