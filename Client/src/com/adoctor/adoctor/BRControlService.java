package com.adoctor.adoctor;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;

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

		unregisterRestartAlarm(); 
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(mScreenStateReceiver, filter);

		return START_REDELIVER_INTENT;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO 어떤 효과가 발생하는건지?
		return null;
	}

	@Override
	public void onDestroy() {
		unregisterReceiver(mScreenStateReceiver);
		registerRestartAlarm();
		super.onDestroy();
	}

	void registerRestartAlarm() {
		Intent intent = new Intent(BRControlService.this, RestartService.class);
		intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				BRControlService.this, 0, intent, 0); // 브로드케스트할 Intent
		long firstTime = SystemClock.elapsedRealtime();  // 현재 시간
		firstTime += 1 * 1000; // 10초 후에 알람이벤트 발생
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE); // 알람 서비스 등록
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
				10 * 1000, sender); // 알람이
	}

	void unregisterRestartAlarm() {
		Intent intent = new Intent(BRControlService.this, RestartService.class);
		intent.setAction(RestartService.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				BRControlService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);    
	}
}