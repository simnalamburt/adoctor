package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Broadcast receiver로서, Screen on, off 될 때 Broadcast를 받아 DB에 시간과 함께 기록
 * 
 * @author Choi H.John, Sky77
 * 
 */
public class ScreenStateReceiver extends BroadcastReceiver {

	// TODO : 하드코딩 (ENUM)
	private static final boolean ON = true;
	private static final boolean OFF = false;
	private static final String iON = Intent.ACTION_SCREEN_ON;
	private static final String iOFF = Intent.ACTION_SCREEN_OFF;

	@Override
	public void onReceive(Context context, Intent intent) {

		long time = System.currentTimeMillis();
		
		boolean state;
		if (intent.getAction().equals(iON)) state = ON;
		else if (intent.getAction().equals(iOFF)) state = OFF;
		else return;
		
		DB.ScreenLog.Insert(time, state);
	}
}