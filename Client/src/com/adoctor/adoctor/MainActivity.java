package com.adoctor.adoctor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.adoctor.adoctor.DB.ScreenLog;
import com.adoctor.adoctor.DB.ScreenLogEntity;
import com.adoctor.adoctor.DB.ScreenState;

/**
 * 최초 Activity. DB의 내용을 가져와 보여줌
 * @author Choi H.John, Sky77, Hyeon
 */
public class MainActivity extends Activity {

	public static final String PREFS_NAME = "Pref";

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
		if (item.getItemId() == R.id.deletebtn) {
			ScreenLog.getInstance().Flush();
			this.onRefreshButton(null);
		} else if (item.getItemId() == R.id.inputdata) {
			inputdata();
		}
		return super.onOptionsItemSelected(item);
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
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		age.setText(settings.getString("age", "0"));
		job.setSelection(settings.getInt("job", 0));
		if(settings.getInt("sex", -1)!=-1) ((RadioButton)v2.findViewById(settings.getInt("sex", 0))).setChecked(true);
		alert.setCancelable(true);
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("age", age.getText().toString());
				editor.putInt("job", job.getSelectedItemPosition());
				editor.putInt("sex", sex.getCheckedRadioButtonId());
				editor.commit();
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