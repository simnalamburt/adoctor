package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ServiceStarter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
			ComponentName cName = new ComponentName(context.getPackageName(), BRControlService.class.getName()); 
			ComponentName svcName = context.startService(new Intent().setComponent(cName));

			if (svcName == null) {
				Log.e("ServiceStarter", "Could not start service " + cName.toString());
			}
		}
	}
}