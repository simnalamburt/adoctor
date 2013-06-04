package com.adoctor.adoctor.DB;

import android.content.Intent;

/**
 * 스크린 상태 열거형
 * @author Hyeon
 */
public enum ScreenState {
	Off {
		@Override
		public int toInt() { return 0; }
		@Override
		public String toString() { return Intent.ACTION_SCREEN_OFF; }
	},
	On {
		@Override
		public int toInt() { return 1; }
		@Override
		public String toString() { return Intent.ACTION_SCREEN_ON; }
	};
	
	
	
	// Static methods
	/**
	 * 정수형을 ScreenState형으로 변환
	 * @param Status 형변환시킬 정수
	 * @return 입력된 정수에 대응되는 ScreenState
	 */
	public static ScreenState fromInt(int Status)
	{
		return Status != 0 ? On : Off;
	}

	/**
	 * 안드로이드 Action을 대응되는 String으로 변환한다. 실패할경우 null 반환
	 * @param Action 안드로이드 Action
	 * @return 안드로이드 Action에 대응되는 String. 실패할경우 null
	 */
	public static ScreenState fromStringOrNull(String Action)
	{
		if (Action.equals(On.toString())) return On;
		else if (Action.equals(Off.toString())) return Off;
		else return null;
	}
	
	
	
	// Non-static methods
	/**
	 * ScreenState형을 int 형으로 변환
	 * @return ScreenState에 대응되는 정수값
	 */
	public abstract int toInt();

	/**
	 * ScreenState에 대응되는 안드로이드 Action을 반환
	 */
	@Override
	public abstract String toString();
}
