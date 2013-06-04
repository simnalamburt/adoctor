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
		
		if (!Preference.hasPref()) inputdata();
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
	private void refresh()
	{
		ScreenLogEntity[] logs = ScreenLog.getInstance().SelectAll();

		String msg = getResources().getString(R.string.log);
		for(ScreenLogEntity log : logs)
			msg += log.Time + "\t" + ( log.State == ScreenState.On ? "켜짐\n" : "꺼짐\n" );
		
		((TextView) findViewById(R.id.logview)).setText(msg);
	}
	
	/**
	 * 정보 입력 창 호출 메서드
	 * 어플리케이션 Preference가 없었을 경우(주로 최초실행시), Preference 입력이 강제된다
	 */
	private void inputdata() { inputdata(null, 0, -1); }
	
	/**
	 * 정보 입력 창 호출 메서드
	 * 입력창에 미리 입력을 주고 싶은 경우 사용
	 * @param Age 나이 (nullable)
	 * @param Job 직업
	 * @param Sex 성별
	 */
	private void inputdata(final Integer Age, final int Job, final int Sex)
	{
		// 레이아웃 로드
		LayoutInflater inf = getLayoutInflater();
		View v2 = inf.inflate(R.layout.inputdata, (ViewGroup)findViewById(R.id.input_layout));
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		alert.setTitle("정보입력");
		alert.setView(v2);
		final TextView age = (TextView)v2.findViewById(R.id.age);
		final Spinner job = (Spinner)v2.findViewById(R.id.job);
		final RadioGroup sex = (RadioGroup)v2.findViewById(R.id.sex);
		
		// 설정 읽어옴
		PreferenceData pref = Preference.getPref();
		if (pref != null) {
			// 기존 설정이 있는경우
			age.setText(Integer.toString(pref.age));
			job.setSelection(pref.job);
			sex.check(pref.sex);
			
			alert.setNegativeButton("취소", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) { }
			});
		} else {
			// 기존 설정이 없는경우
			if (Age != null) age.setText(Integer.toString(Age));
			job.setSelection(Job);
			sex.check(Sex);
			
			alert.setCancelable(false);
		}
		
		alert.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String textAge = age.getText().toString();
				
				Integer inputAge = textAge.equals("") ? null : Integer.parseInt(textAge);
				int inputJob = job.getSelectedItemPosition();
				int inputSex = sex.getCheckedRadioButtonId();
				
				if (inputAge == null) {
					Toast.makeText(App.getContext(), "나이를 입력해주세요 :)", Toast.LENGTH_SHORT).show();
					inputdata(inputAge, inputJob, inputSex);
					return;
				}
				
				if (inputSex == -1) {
					Toast.makeText(App.getContext(), "성별을 입력해주세요", Toast.LENGTH_SHORT).show();
					inputdata(inputAge, inputJob, inputSex);
					return;
				}
				
				Preference.setPref(inputAge, inputJob, inputSex);
			}
		});
		
		alert.show();
	}
	
}