package com.adoctor.adoctor;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;

/**
 * ScreenStateReceiver를 등록하는 Service
 * @author Choi H.John, Sky77
 * 
 */
public class BRControlService extends Service {

	ScreenStateReceiver mScreenStateReceiver = new ScreenStateReceiver();
	Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.status_bar_icon);

	/**
	 * ScreenStateReceiver 등록
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Notification notification = new Notification.Builder(getBaseContext())
        .setContentTitle("ADoctor 실행중입니다. ")
        .setContentText("통계 조사에 참여해 주셔서 감사합니다.")
        .setSmallIcon(R.drawable.status_bar_icon)
        .setLargeIcon(largeIcon)
        .build();
				
		 
	    Intent main = new Intent(this, MainActivity.class);
	    main.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
	    //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, main,  PendingIntent.FLAG_UPDATE_CURRENT);
	
	    //notification.setLatestEventInfo(this, "title", "text", pendingIntent);
	    notification.flags |= Notification.FLAG_ONGOING_EVENT | Notification.FLAG_FOREGROUND_SERVICE | Notification.FLAG_NO_CLEAR;
	 
	    startForeground(889901, notification);

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
		Intent intent = new Intent(BRControlService.this, ServiceRestarter.class);
		intent.setAction(ServiceRestarter.ACTION_RESTART_PERSISTENTSERVICE);
		PendingIntent sender = PendingIntent.getBroadcast(
				BRControlService.this, 0, intent, 0); // 브로드케스트할 Intent
		long firstTime = SystemClock.elapsedRealtime();  // 현재 시간
		firstTime += 1 * 1000; // 10초 후에 알람이벤트 발생
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE); // 알람 서비스 등록
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime, 10 * 1000, sender);
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

		// 내일 0시 0분에 처음 시작해서, 24시간 마다 실행되게
		GregorianCalendar tomorrow = new GregorianCalendar();
		tomorrow = new GregorianCalendar(tomorrow.get(Calendar.YEAR),tomorrow.get(Calendar.MONTH),tomorrow.get(Calendar.DATE));
		tomorrow.add(Calendar.DATE, 1);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, tomorrow.getTimeInMillis(), AlarmManager.INTERVAL_DAY, sender);
	}
	
	public void unregisterSendAlarm() {
		Intent intent = new Intent(this, LogSender.class);
		PendingIntent sender = PendingIntent.getBroadcast(this, 1, intent, 0);

		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.cancel(sender);
	}
}