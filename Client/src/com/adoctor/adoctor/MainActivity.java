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

	/**
	 * 프로그램 진입점
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		startService(new Intent(this, BRControlService.class));
		onRefreshButton(null);
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
		
		((TextView) findViewById(R.id.logview)).setText(msg);
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
	 * 메뉴버튼을 눌렀을 때 호출됨.
	 * 로그삭제버튼을 눌렀을경우, DB의 내용을 서버로 전송한 후 로그가 삭제됨
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.deletebtn)
		{
			ScreenLog.getInstance().Flush();
			this.onRefreshButton(null);
		}
		return super.onOptionsItemSelected(item);
	}
}