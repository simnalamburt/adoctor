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

/**
 * 화면에 보여지는 Activity로, DB의 내용을 가져와 보여줌 일단은 전부 보여주게 코딩함
 * 
 * @author Choi H.John, Sky77, Hyeon
 * 
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
	 * 서비스를 시작함 서비스 시작 버튼을 눌렀을 때 호출됨
	 * 
	 * @param v
	 */
	public void onStartserviceButton(View v) {
		startService(new Intent(this, BRControlService.class));
	}

	/**
	 * DB의 내용을 가져와 TextView에 뿌려줌 새로고침 버튼을 눌렀을 때 호출됨
	 * 
	 * @param v
	 *            눌러진 버튼 View
	 */
	public void onRefreshButton(View v) {
		ScreenLogEntity[] logs = ScreenLog.getInstance().SelectAll();

		String msg = getResources().getString(R.string.log);
		for(ScreenLogEntity log : logs)
			msg += log.Time + "\t\t\t" + log.State + '\n';
		
		logview.setText(msg);
	}

	/**
	 * scrlog 테이블의 내용을 비움 메뉴의 로그삭제 버튼을 눌렀을 때 호출됨
	 * 
	 * @param i
	 */
	public void onDeleteButton(MenuItem i) {
		ScreenLog.getInstance().Flush();
	}

	/**
	 * 아무 문자열이나 TCP/UTF-8로 서버에 전송. 전송 버튼을 눌렀을 때 호출됨
	 * TODO 네트워킹 구현 완성하기
	 * @param v
	 */
	public void onSendButton(View v) {
		new NetworkTask().execute("안드로이드 메세지");
	}

	/**
	 * 메뉴 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * 메뉴 버튼 눌렀을 때 호출됨
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.deletebtn:
			onDeleteButton(item);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}