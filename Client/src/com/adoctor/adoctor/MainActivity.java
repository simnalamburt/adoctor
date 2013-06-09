package com.adoctor.adoctor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Message;
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
import com.adoctor.adoctor.Network.Network;
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
		refreshScreenLog();
		if (!Preference.hasPref()) editPreference();
		
		Network.AddHandler(onSendDone);
	}
	/**
	 * DB의 내용을 가져와 TextView에 뿌려줌 새로고침 버튼을 눌렀을 때 호출됨
	 * @param v 함수를 호출한 View 객체. 사용하지 않음.
	 */
	public void onRefreshButton(View v) {
		refreshScreenLog();
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
			flushScreenLog();
			return true;
		case R.id.inputdata:
			editPreference();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}		
	}
	


	// 기능 정의
	/**
	 * 정보 입력 창 호출 메서드
	 * 어플리케이션 Preference가 없었을 경우(주로 최초실행시), Preference 입력이 강제된다
	 */
	public void editPreference() { editPreference(null, 0, -1); }
	/**
	 * 정보 입력 창 호출 메서드
	 * 입력창에 미리 입력을 주고 싶은 경우 사용
	 * @param Age 나이 (nullable)
	 * @param Job 직업
	 * @param Sex 성별
	 * @param DSTime 하루 시작 시간
	 */
	private void editPreference(Integer Age, int Job, int Sex)
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

			if (pref.sex == 0) sex.check(R.id.sex_male);
			else if (pref.sex == 1) sex.check(R.id.sex_female);

			alert.setNegativeButton("취소", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int which) { }
			});
		} else {
			// 기존 설정이 없는경우
			if (Age != null) age.setText(Age.toString());
			job.setSelection(Job);

			if (Sex == 0) sex.check(R.id.sex_male);
			else if (Sex == 1) sex.check(R.id.sex_female);

			alert.setCancelable(false);
		}

		alert.setPositiveButton("확인", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				String textAge = age.getText().toString();
				int radioid = sex.getCheckedRadioButtonId();

				Integer inputAge = textAge.equals("") ? null : Integer.parseInt(textAge);
				int inputJob = job.getSelectedItemPosition();
				int inputSex = radioid == -1 ? -1 : ( radioid == R.id.sex_male ? 0 : 1 );

				if (inputAge == null) {
					Toast.makeText(App.getContext(), "나이를 입력해주세요 :)", Toast.LENGTH_SHORT).show();
					editPreference(inputAge, inputJob, inputSex);
					return;
				}

				if (inputSex == -1) {
					Toast.makeText(App.getContext(), "성별을 입력해주세요", Toast.LENGTH_SHORT).show();
					editPreference(inputAge, inputJob, inputSex);
					return;
				}

				Preference.setPref(inputAge, inputJob, inputSex);
				refreshScreenLog();
			}
		});

		alert.show();
	}

	/**
	 * 로그 새로고침 메서드
	 */
	public void refreshScreenLog()
	{
		ScreenLogEntity[] logs = ScreenLog.getInstance().SelectAll();

		String msg = getResources().getString(R.string.log);
		DateFormat format = SimpleDateFormat.getTimeInstance();

		long TotalUsage = 0;
		for(ScreenLogEntity log : logs)
		{
			msg += format.format(log.Time) + "에 " + Double.toString( log.Duration / 1000.0) + "초\n";
			TotalUsage += log.Duration;
		}
		
		if(TotalUsage > 0)
			msg += "켜져있던 총 시간 : "
					+ (TotalUsage/3600000 !=0 ? TotalUsage/3600000+"시간 ":"" )
					+ (TotalUsage/60000 !=0 ? (TotalUsage%3600000)/60000+"분 ":"" )
					+ Double.toString((TotalUsage%60000)/1000.0) + "초\n";

		((TextView)findViewById(R.id.logview)).setText(msg);
	}
	/**
	 * 로그 비움&전송 메서드
	 */
	public void flushScreenLog()
	{
		new Network().execute();
	}
	/**
	 * 로그가 비워졌을때 호출되는 콜백함수
	 */
	private WeakReferencedHandler<MainActivity> onSendDone = new WeakReferencedHandler<MainActivity>(this) {
		@Override
		public void handleMessage(MainActivity instance, Message msg) {
			switch (msg.what)
			{
			case Network.NETWORK_SUCCEEDED:
				instance.refreshScreenLog();
				break;
			case Network.NETWORK_FAILED:
				Exception exception = (Exception) msg.obj;
				Toast.makeText(App.getContext(), exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
}