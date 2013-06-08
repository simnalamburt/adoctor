package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adoctor.adoctor.DB.ScreenLog;

/**
 * Broadcast receiver로서, Screen on, off 될 때 Broadcast를 받아 DB에 시간과 함께 기록
 * @author Choi H.John, Sky77, Hyeon
 */
public class ScreenStateReceiver extends BroadcastReceiver {
	
	long TimeScreenOn;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action == Intent.ACTION_SCREEN_ON)
		{
			TimeScreenOn = System.currentTimeMillis();
		} else if (action == Intent.ACTION_SCREEN_OFF) {
			long timeScreenOff = System.currentTimeMillis(); 
			int duration = (int) (timeScreenOff - TimeScreenOn);
			ScreenLog.getInstance().Insert(TimeScreenOn, duration);
		}
	}
}