package com.goznauk.adoctor;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class ScreenStateReceiver extends BroadcastReceiver
 {
	private static String TAG = "[ADoctor_BR]";
	
    public static boolean screenstate;
	public static String time;
	
	private final boolean ON = true;
	private final boolean OFF = false;
	private final String iON = Intent.ACTION_SCREEN_ON;
	private final String iOFF = Intent.ACTION_SCREEN_OFF;
	
	@Override
	public void onReceive (Context context, Intent intent) {
		Toast.makeText(context, "onReceive", Toast.LENGTH_SHORT).show();
		Log.i(TAG, "onReceive");
		
		Date dtime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yy,MM,dd,hh,mm,ss");
		time = sdf.format(dtime);
		
		if (intent.getAction().equals(iON)) {
			screenstate = ON;
			Toast.makeText(context, "BR catched screen is on", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "BR catched screen is on");
		}
		if (intent.getAction().equals(iOFF)) {
			screenstate = OFF;
		}
		
		Intent toServiceIntent = new Intent(context, ADoctorService.class);
		toServiceIntent.putExtra("screen_state", screenstate)
		               .putExtra("time", time);
		context.startService(toServiceIntent);
	}
}
