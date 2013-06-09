package com.adoctor.adoctor;

import java.lang.ref.WeakReference;

import android.os.Handler;
import android.os.Message;

public abstract class WeakReferencedHandler<T> extends Handler {

	private WeakReference<T> weakreference;
	
	public WeakReferencedHandler(T instance)
	{
		weakreference = new WeakReference<T>(instance);
	}

	@Override
	public final void handleMessage(Message msg) {
		super.handleMessage(msg);
		
		T instance = weakreference.get();
		if (instance != null) handleMessage(instance, msg);
	}
	
	public abstract void handleMessage(T instance, Message msg);
}
