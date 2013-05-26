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

	/**
	 * ScreenStateReceiver 등록
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenStateReceiver, filter);

		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO : 어떤 효과가 발생하는건지?
		return null;
	}
}