package com.adoctor.adoctor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.adoctor.adoctor.DB.ScreenLog;
import com.adoctor.adoctor.DB.ScreenLogEntity;
import com.adoctor.adoctor.DB.ScreenState;
import com.adoctor.adoctor.pref.Preference;
import com.adoctor.adoctor.pref.PreferenceData;

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
		refresh();
	}
	/**
	 * DB의 내용을 가져와 TextView에 뿌려줌 새로고침 버튼을 눌렀을 때 호출됨
	 * @param v 함수를 호출한 View 객체. 사용하지 않음.
	 */
	public void onRefreshButton(View v) {
		refresh();
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
		switch(item.getItemId())
		{
		case R.id.deletebtn:
			ScreenLog.getInstance().Flush();
			refresh();
			return true;
		case R.id.inputdata:
			inputdata();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}

	
	
	// 기능 정의
	/**
	 * 로그 새로고침 메서드
	 */
	public void refresh()
	{
		ScreenLogEntity[] logs = ScreenLog.getInstance().SelectAll();

		String msg = getResources().getString(R.string.log);
		for(ScreenLogEntity log : logs)
			msg += log.Time + "\t" + ( log.State == ScreenState.On ? "켜짐\n" : "꺼짐\n" );
		
		((TextView) findViewById(R.id.logview)).setText(msg);
	}
	
	/**
	 * 정보 입력 창 호출 메서드
	 */
	public void inputdata()
	{
		LayoutInflater inf = getLayoutInflater();
		View v2 = inf.inflate(R.layout.inputdata, (ViewGroup)findViewById(R.id.input_layout));
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("정보입력");
		alert.setView(v2);
		final TextView age = (TextView)v2.findViewById(R.id.age);
		final Spinner job = (Spinner)v2.findViewById(R.id.job);
		final RadioGroup sex = (RadioGroup)v2.findViewById(R.id.sex);
		
		PreferenceData pref = Preference.getPref();
		if (pref != null)
		{
			age.setText(Integer.toString(pref.age));
			job.setSelection(pref.job);
			sex.check(pref.sex);
		}
		
		alert.setCancelable(true);
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (age.getText().length() == 0) {
					Toast.makeText(App.getContext(), "나이를 입력해주세요", Toast.LENGTH_SHORT).show();
					inputdata();
				} else {
					Preference.setPref(
						Integer.parseInt(age.getText().toString()),
						job.getSelectedItemPosition(),
						sex.getCheckedRadioButtonId());
				}
			}
		});
		alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alert.show();
	}
}