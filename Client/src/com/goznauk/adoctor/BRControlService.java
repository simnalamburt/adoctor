package com.goznauk.adoctor;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Broadcast receiver
 * @author 훈존
 */
public class BRControlService extends Service {

	ScreenStateReceiver mScreenStateReceiver = new ScreenStateReceiver();

	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service onCreate", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// service destroyed log method
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		// String text = intent.getStringExtra("time") + " : "
		// + intent.getBooleanExtra("screen_state", true);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenStateReceiver, filter);

		Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();

		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
