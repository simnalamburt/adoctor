package com.goznauk.adoctor;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ADoctorServiceManager extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// 부팅 시 서비스 실행
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			Intent toServiceIntent = new Intent(context, ADoctorService.class);
			context.startService(toServiceIntent);
		}
		
		/*	ComponentName cName = new ComponentName(context.getPackageName(), ADoctor);
			ComponentName svcName = context.startService(new Intent().setComponent(cName);
			if (svc == null) {
				Log.e(TAG, "Could not start service " + cName.toString());
				}
			} else {
			   Log.e(TAG, "Received unexpected intent " + intent.toString());
			}
		 */
	}
}

