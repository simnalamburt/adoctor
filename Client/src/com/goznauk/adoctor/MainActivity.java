package com.goznauk.adoctor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 화면에 보여지는 Activity로, DB의 내용을 가져와 보여줌
 * 일단은 전부 보여주게 코딩함
 * @author 훈존
 *
 */
public class MainActivity extends Activity {

	Context mCtx = this;
	TextView mTV;

	/**
	 * 프로그램 진입점
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO : 디버그용 메세지
		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

		// Intent intent = new Intent(this, BRControlService.class);
		// startService(intent);

		mTV = (TextView) findViewById(R.id.textview);

		// Start Service 버튼 onClick 이벤트 핸들러에 이벤트 등록
		Button button = (Button) findViewById(R.id.startservicebtn);
		button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mCtx, BRControlService.class);
				startService(intent);
			}
		});

		// Refresh 버튼 onClick 이벤트 핸들러에 이벤트 등록
		Button RefreshBtn = (Button) findViewById(R.id.refreshbtn);
		RefreshBtn.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				refresh();
			}
		});
	}

	/**
	 * 새로고침 버튼 누르면 호출됨
	 * DB의 내용을 가져와 TextView에 뿌려줌
	 */
	public void refresh() {
		DBAdapter adb = new DBAdapter(this, DBAdapter.SQL_CREATE_SCRLOG, "scrlog");
		adb.open();

		String columns[] = { "time", "screenstate" };
		Cursor c = adb.selectTable(columns, null, null, null, null, null);

		mTV.setText("Screen State Log");

		if (c.moveToFirst()) {
			do {
				mTV.append(c.getString(0) + ":" + c.getString(1) + "\n");
			} while (c.moveToNext());
		}

		adb.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}