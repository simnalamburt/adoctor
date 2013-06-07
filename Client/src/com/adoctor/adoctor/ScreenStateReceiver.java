package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adoctor.adoctor.DB.ScreenLog;
import com.adoctor.adoctor.DB.ScreenState;

/**
 * Broadcast receiver로서, Screen on, off 될 때 Broadcast를 받아 DB에 시간과 함께 기록
 * @author Choi H.John, Sky77, Hyeon
 */
public class ScreenStateReceiver extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		ScreenState state = ScreenState.fromStringOrNull(intent.getAction());
		if (state != null) {
			long time = System.currentTimeMillis();
			ScreenLog.getInstance().Insert(time, state);
		}
	}
}