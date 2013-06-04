package com.adoctor.adoctor.DB;

import java.io.IOException;

import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;



/**
 * ScreenLog 테이블 엔티티 클래스
 * @author Hyeon
 */
public class ScreenLogEntity implements MessagePackable {
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

	@Override
	public void readFrom(Unpacker arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTo(Packer arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
