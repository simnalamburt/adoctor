package com.goznauk.adoctor;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

/**
 * ScreenStateReceiver를 등록하는 Service
 * @author Choi H.John, Sky77
 * 
 */
public class BRControlService extends Service {

	ScreenStateReceiver mScreenStateReceiver = new ScreenStateReceiver();

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	/**
	 * ScreenStateReceiver 등록
	 */
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		// String text = intent.getStringExtra("time") + " : "
		// + intent.getBooleanExtra("screen_state", true);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenStateReceiver, filter);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// service destroyed log method
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}