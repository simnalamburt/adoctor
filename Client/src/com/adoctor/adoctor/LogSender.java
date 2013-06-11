package com.adoctor.adoctor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.adoctor.adoctor.Network.Network;

public class LogSender extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		new Network().execute();
	}

}
