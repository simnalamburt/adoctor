package com.goznauk.adoctor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

/**
 * 화면에 보여지는 Activity로, DB의 내용을 가져와 보여줌 일단은 전부 보여주게 코딩함
 * 
 * @author Choi H.John, Sky77
 * 
 */
public class MainActivity extends Activity {

	TextView logview;
	NetworkTask networking;
	
	/**
	 * 프로그램 진입점
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		logview = (TextView) findViewById(R.id.logview);
		networking = new NetworkTask(this);
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
		// TODO : 하드코딩 (테이블 이름)
		DBAdapter adb = new DBAdapter(this, "scrlog");
		adb.open();

		// TODO : 하드코딩 (칼럼 이름)
		String columns[] = { "time", "screenstate" };
		Cursor c = adb.selectTable(columns, null, null, null, null, null);

		String msg = getResources().getString(R.string.log);
		if (c.moveToFirst()) {
			do
				msg += c.getLong(0) + "\t\t\t" + c.getString(1) + '\n';
			while (c.moveToNext());
		}

		adb.close();

		logview.setText(msg);
	}

	public void onSendButton(View v) {
		networking.execute("안드로이드 메세지");
	}

	/**
	 * scrlog 테이블의 내용을 비움 메뉴의 로그삭제 버튼을 눌렀을 때 호출됨
	 * 
	 * @param i
	 */
	public void onDeleteButton(MenuItem i) {
		DBAdapter adb = new DBAdapter(this, "scrlog");
		adb.open();
		adb.query("DELETE FROM scrlog");
		adb.close();
	}

	/**
	 * 메뉴 생성
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
}