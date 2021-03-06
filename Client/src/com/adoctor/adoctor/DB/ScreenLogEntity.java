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
	public int Duration;
	
	/**
	 * ScreenLog 엔티티 인스턴스 생성
	 * @param Time 화면 상태가 변화한 시간
	 * @param State 화면의 상태
	 */
	public ScreenLogEntity(long Time, int Duration)
	{
		this.Time = Time;
		this.Duration = Duration;
	}

	// MessagePack
	/**
	 * ScreenLogEntity 시리얼라이저
	 */
	@Override
	public void writeTo(Packer pk) throws IOException {
		pk.writeArrayBegin(2);
		pk.write(Time);
		pk.write(Duration);
		pk.writeArrayEnd();
	}
	/**
	 * ScreenLogEntity 디시리얼라이저
	 */
	@Override
	public void readFrom(Unpacker u) throws IOException {
		u.readArrayBegin();
		Time = u.readLong();
		Duration = u.readInt();
		u.readArrayEnd(true);
	}
}
