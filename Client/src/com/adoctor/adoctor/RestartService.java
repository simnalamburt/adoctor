package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class RestartService extends BroadcastReceiver {

	public static final String ACTION_RESTART_PERSISTENTSERVICE = "RESTART";

	@Override
	public void onReceive(Context context, Intent intent) {

	        if(intent.getAction().equals(ACTION_RESTART_PERSISTENTSERVICE)) {
	               Intent i = new Intent(context, BRControlService.class);
	                context.startService(i);
	        }      
	}

}
