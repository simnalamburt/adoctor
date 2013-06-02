package com.adoctor.adoctor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.adoctor.adoctor.DB.ScreenLog;
import com.adoctor.adoctor.DB.ScreenLogEntity;
import com.adoctor.adoctor.DB.ScreenState;

/**
 * 최초 Activity. DB의 내용을 가져와 보여줌
 * @author Choi H.John, Sky77, Hyeon
 */
public class MainActivity extends Activity {

	TextView logview;

	/**
	 * 프로그램 진입점
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		logview = (TextView) findViewById(R.id.logview);
	}

	/**
	 * 서비스 시작. 서비스 시작 버튼을 눌렀을 때 호출됨
	 * @param v 사용하지 않는 입력
	 */
	public void onStartserviceButton(View v) {
		startService(new Intent(this, BRControlService.class));
	}

	/**
	 * DB의 내용을 가져와 TextView에 뿌려줌 새로고침 버튼을 눌렀을 때 호출됨
	 * @param v 함수를 호출한 View 객체. 사용하지 않음.
	 */
	public void onRefreshButton(View v) {
		ScreenLogEntity[] logs = ScreenLog.getInstance().SelectAll();

		String msg = getResources().getString(R.string.log);
		for(ScreenLogEntity log : logs)
			msg += log.Time + "\t" + ( log.State == ScreenState.On ? "켜짐\n" : "꺼짐\n" );
		
		logview.setText(msg);
	}

	/**
	 * 아무 문자열이나 TCP/UTF-8로 서버에 전송. 전송 버튼을 눌렀을 때 호출됨
	 * @param v 함수를 호출한 View 객체. 사용하지 않음
	 */
	public void onSendButton(View v) {
		ScreenLog.getInstance().Flush();
		this.onRefreshButton(null);
	}
	

	
	// 메뉴
	/**
	 * 메뉴 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 메뉴버튼을 눌렀을 때 호출되는 메서드
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.deletebtn) onSendButton(null);
		return super.onOptionsItemSelected(item);
	}
}