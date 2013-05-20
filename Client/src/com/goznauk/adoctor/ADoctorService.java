package com.goznauk.adoctor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class ADoctorService extends Service {
    
	private static String TAG = "[ADoctor_Service]";
	File myfile = getDir("myfile", Activity.MODE_APPEND);
	//public String mPath = myfile.getAbsolutePath() + "/log.txt";
	
	ScreenStateReceiver screenstateReceiver = new ScreenStateReceiver();
	
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "Service onCreated", Toast.LENGTH_SHORT).show();
		Log.i(TAG, "Service OnCreate 호출됨");
		
		//file check, 없으면 생성
		
		
		Toast.makeText(this, "File Checked", Toast.LENGTH_SHORT).show();
		Log.i(TAG, "File Checked");
		
		//Receiver 등록
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenstateReceiver, filter);
		
		Toast.makeText(this, "Receiver Registered", Toast.LENGTH_SHORT).show();
		Log.i(TAG, "Reciever Registered");
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		//service destroyed log method
	}
	
	
	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();
		Log.i(TAG, "onStartCommand");
		
		//log file 기록
		String text = intent.getStringExtra("time") + " : "
	              + intent.getBooleanExtra("screen_state", true);
		
		File file = new File("log.txt");
		FileOutputStream fos = null;
		
		if (file.exists() == false) {
			try {
			file.createNewFile();
			} catch (IOException e) {
			}
		}

		
		try {
			fos = new FileOutputStream(file, true);
			Toast.makeText(this, "file opened", Toast.LENGTH_SHORT).show();
			
			if (fos != null) {
				fos.write(text.getBytes());
				fos.write("\n".getBytes());
				
				Toast.makeText(this, "write", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			//fail
		} finally {
			try {
				if (fos != null) {
					fos.close();
					Toast.makeText(this, "file closed", Toast.LENGTH_SHORT).show();
				}
			} catch (Exception e) { ; }
		}
		
		Log.i(TAG, "File 기록됨");
		Toast.makeText(this, "File 기록됨", Toast.LENGTH_SHORT).show();
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			
		
		//Receiver 등록
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(screenstateReceiver, filter);
		
		Toast.makeText(this, "Receiver Re-Registered", Toast.LENGTH_SHORT).show();
		Log.i(TAG, "Receiver Re-Registered");
		
		//STICKY
		return START_STICKY;
		
		//Foreground 처리
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
}
