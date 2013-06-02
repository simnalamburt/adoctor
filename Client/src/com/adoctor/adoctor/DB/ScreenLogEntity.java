package com.adoctor.adoctor.DB;

/**
 * ScreenLog 테이블 엔티티 클래스
 * @author Hyeon
 */
public class ScreenLogEntity {
	// Non-static Member&Methods
	public long Time;
	public ScreenState State;
	
	/**
	 * ScreenLog 엔티티 인스턴스 생성
	 * @param Time 화면 상태가 변화한 시간
	 * @param State 화면의 상태
	 */
	public ScreenLogEntity(long Time, ScreenState State)
	{
		this.Time = Time;
		this.State = State;
	}
}
