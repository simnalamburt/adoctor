package com.adoctor.adoctor;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
		registerSendAlarm();

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
		unregisterSendAlarm();
		super.onDestroy();
	}

	void registerRestartAlarm() {
		Intent intent = new Intent(this, ServiceRestarter.class);
		intent.setAction(ServiceRestarter.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				BRControlService.this, 0, intent, 0); // 브로드케스트할 Intent
		long start_time = SystemClock.elapsedRealtime();  // 현재 시간
		start_time += 1 * 1000; // 10초 후에 알람이벤트 발생
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE); // 알람 서비스 등록
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, start_time, 1 * 1000, sender);
	}

	void unregisterRestartAlarm() {
		Intent intent = new Intent(BRControlService.this, ServiceRestarter.class);
		intent.setAction(ServiceRestarter.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				BRControlService.this, 0, intent, 0);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(sender);    
	}

	public void registerSendAlarm() {
		Intent intent = new Intent(this, LogSender.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, 0);

		GregorianCalendar start_time = new GregorianCalendar();
		start_time = new GregorianCalendar(
				start_time.get(Calendar.YEAR),
				start_time.get(Calendar.MONTH),
				start_time.get(Calendar.DATE),
				start_time.get(Calendar.HOUR_OF_DAY),
				((start_time.get(Calendar.MINUTE)<30)?30:0));
		if(start_time.get(Calendar.MINUTE)>30) start_time.add(Calendar.HOUR_OF_DAY, 1); 
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setInexactRepeating(AlarmManager.RTC_WAKEUP, start_time.getTimeInMillis(), AlarmManager.INTERVAL_HALF_HOUR, sender);
	}

	public void unregisterSendAlarm() {
		Intent intent = new Intent(this, LogSender.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, 0);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}
}