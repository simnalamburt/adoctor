package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * BRControlService에서 OnDestroy 호출시 BRControlService를 다시 부활시킴
 * @author Sky77
 */
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
