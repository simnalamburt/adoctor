package com.goznauk.adoctor;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Broadcast receiver로서, Screen on, off 될 때 Broadcast를 받아 DB에 시간과 함께 기록
 * @author Choi H.John, Sky77
 *
 */
public class ScreenStateReceiver extends BroadcastReceiver {

	public static boolean screenstate;
	public static long time;

	private final boolean ON = true;
	private final boolean OFF = false;
	private final String iON = Intent.ACTION_SCREEN_ON;
	private final String iOFF = Intent.ACTION_SCREEN_OFF;

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();

		time = System.currentTimeMillis();

		DBAdapter adb = new DBAdapter(context, "scrlog");
		adb.open();

		ContentValues cv = new ContentValues();

		if (intent.getAction().equals(iON)) {
			screenstate = ON;

			cv.put("time", time);
			cv.put("screenstate", screenstate);
			adb.insertTable(cv);
			adb.close();

			Toast.makeText(context,"BR catched screen is on" + time + screenstate,
							Toast.LENGTH_SHORT).show();
		}
		if (intent.getAction().equals(iOFF)) {
			screenstate = OFF;

			cv.put("time", time);
			cv.put("screenstate", screenstate);
			adb.insertTable(cv);
			adb.close();

		}

		// Intent toServiceIntent = new Intent(context, BRControlService.class);
		// toServiceIntent.putExtra("screenstate", screenstate)
		// .putExtra("time", time);
		// context.startService(toServiceIntent);
	}
}