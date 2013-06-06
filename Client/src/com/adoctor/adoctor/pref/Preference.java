package com.adoctor.adoctor.pref;

import com.adoctor.adoctor.App;

import android.content.SharedPreferences;

/**
 * 어플리케이션 Preference 래퍼 클래스
 * @author Sky77, Hyeon, H.John
 */
public class Preference {

	private static final String PREFS_NAME = "Pref";

	/**
	 * 어플리케이션 설정이 있는지 여부를 확인
	 * @return 설정이 있을경우 true, 없을경우 false
	 */
	public static boolean hasPref() {
		SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
		return settings.getBoolean("hasPref", false);
	}
	
	/**
	 * 어플리케이션 설정을 반환
	 * @return 설정이 있을경우 PreferenceData 객체로 정보를 반환, 없을경우 null을 반환
	 */
	public static PreferenceData getPref() {
		SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
		if (settings.getBoolean("hasPref", false))
			return new PreferenceData(
				settings.getInt("age", 0),
				settings.getInt("job", 0),
				settings.getInt("sex", 0),
				settings.getLong("dstime", 6*3600*1000));
		else return null;
	}
	
	/**
	 * 어플리케이션 설정을 수정
	 *@param Age 나이
	 * @param Job 직업
	 * @param Sex 성별
	 * @param DSTime 하루 시작 시간
	 */
	public static void setPref(int Age, int Job, int Sex, long DSTime) {
		SharedPreferences settings = App.getContext().getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("hasPref", true);
		editor.putInt("age", Age);
		editor.putInt("job", Job);
		editor.putInt("sex", Sex);
		editor.putLong("dstime", DSTime);
		editor.commit();
	}
}
