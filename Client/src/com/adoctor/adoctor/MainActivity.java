package com.adoctor.adoctor;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
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
public class MainActivity extends Activity implements OnTimeChangedListener {


	Clock01 mClock01;
	Handler mHandler;
	Calendar DSTimeCal;
	long total_time = 0;

	/**
	 * 프로그램 진입점
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mClock01 = (Clock01) findViewById(R.id.clock01);
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				mClock01.setTimeSum(refresh());
			}
		};
		DSTimeCal = Calendar.getInstance();

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
	private long refresh()
	{
		ScreenLogEntity[] logs = ScreenLog.getInstance().SelectAll();

		String msg = getResources().getString(R.string.log);
		SimpleDateFormat format = new SimpleDateFormat("yy.MM.dd hh:mm:ss");

		for(ScreenLogEntity log : logs)
			msg += format.format(log.Time) + "\t" + ( log.State == ScreenState.On ? "켜짐\n" : "꺼짐\n" );

		boolean swch = false;
		long on_time = 0;
		for(ScreenLogEntity log : logs) {
			if(swch) {
				if(log.State==ScreenState.On) on_time=log.Time;
				else {
					total_time+=log.Time-on_time;
					swch=false;
				}
			}

			else {
				if(log.State==ScreenState.On) {
					swch=true;
					on_time=log.Time;
				}
			}	
		}
		if(total_time!=0) msg += "켜져있던 총 시간 : "+ (total_time/3600000 !=0 ? total_time/3600000+"시간 ":"" )+( total_time/60000 !=0 ? (total_time%3600000)/60000+"분 ":"" )+(total_time%60000)/1000+"초\n";

		((TextView)findViewById(R.id.logview)).setText(msg);
		mClock01.setTimeSum(total_time);

		return total_time;
	}

	/**
	 * 정보 입력 창 호출 메서드
	 * 어플리케이션 Preference가 없었을 경우(주로 최초실행시), Preference 입력이 강제된다
	 */
	private void inputdata() { inputdata(null, 0, -1, 6*3600*1000); }

	/**
	 * 정보 입력 창 호출 메서드
	 * 입력창에 미리 입력을 주고 싶은 경우 사용
	 * @param Age 나이 (nullable)
	 * @param Job 직업
	 * @param Sex 성별
	 * @param DSTime 하루 시작 시간
	 */
	private void inputdata(Integer Age, int Job, int Sex, long DSTime)
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

		final TimePicker daystart = (TimePicker)v2.findViewById(R.id.dstimepicker);

		daystart.setOnTimeChangedListener(this);
		final long dstime = DSTimeCal.getTimeInMillis();


		// 설정 읽어옴
		PreferenceData pref = Preference.getPref();
		if (pref != null) {
			// 기존 설정이 있는경우
			age.setText(Integer.toString(pref.age));
			job.setSelection(pref.job);

			Calendar cal1 = Calendar.getInstance();
			cal1.setTimeInMillis(pref.dstime);
			daystart.setCurrentHour(cal1.get(Calendar.HOUR_OF_DAY));
			daystart.setCurrentMinute(cal1.get(Calendar.MINUTE));
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
			Calendar cal2 = Calendar.getInstance();
			cal2.setTimeInMillis(DSTime);
			daystart.setCurrentHour(cal2.get(Calendar.HOUR_OF_DAY));
			daystart.setCurrentMinute(cal2.get(Calendar.MINUTE));

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
					inputdata(inputAge, inputJob, inputSex, dstime);
					return;
				}

				if (inputSex == -1) {
					Toast.makeText(App.getContext(), "성별을 입력해주세요", Toast.LENGTH_SHORT).show();
					inputdata(inputAge, inputJob, inputSex, dstime);
					return;
				}

				Preference.setPref(inputAge, inputJob, inputSex, dstime);
				refresh();
			}
		});

		alert.show();
	}

	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		DSTimeCal = Calendar.getInstance();

		DSTimeCal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		DSTimeCal.set(Calendar.MINUTE, minute);
		DSTimeCal.set(Calendar.SECOND, 0);

	}	

}