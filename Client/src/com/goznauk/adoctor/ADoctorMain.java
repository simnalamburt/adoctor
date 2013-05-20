package com.goznauk.adoctor;

import java.io.BufferedReader;
import java.io.FileReader;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.adoctor.R;


public class ADoctorMain extends Activity
{
    /** Called when the activity is first created. */
    TextView ResultView;
	static String mPath;
	static String TAG = "[ADoctor]";
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        Log.i(TAG, "OnCreate 호출됨");
        
        setContentView(R.layout.adoctor_main);
        ResultView = (TextView)findViewById(R.id.resultView);
        Log.i(TAG, "화면 구성됨");
		
		/*		
		int ch;
		
		StringBuilder Result = new StringBuilder();
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(mPath));
			if (in != null) {
				for (;;) {
					ch = in.read();
					if (ch==-1) break;
					Result.append((char)ch);
				}
			}
			
		} catch (Exception e) {
			Result.append("log file not found");
		} finally {
			try {
				if (in != null) in.close();
			} catch (Exception e) {;}
		}
		
		//Result to sResult to TextView
		String sResult = Result.toString();
		/* 역순 출력
		String[] lines = sResult.split("\n");
	    Result.delete(0, Result.length());
		for (int i = lines.length -1; i>=0; i--) {
			Result.append(lines[i]);
			Result.append("\n");
		} 
		sResult = Result.toString(); */
		
		//ResultView.setText(sResult);
    }
}
