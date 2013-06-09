package com.adoctor.adoctor.DB;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;

import com.adoctor.adoctor.WeakReferencedHandler;
import com.adoctor.adoctor.Network.Network;

/**
 * ScreenLog 테이블 싱글톤 클래스
 * @author Hyeon
 */
public class ScreenLog extends Table {
	static final int TABLE_VERSION = 2;
	
	
	
	// Non-static methods
	/**
	 * ScreenLog 클래스 생성자. 외부 클래스로부터의 인스턴스화가 금지되어있음.
	 */
	private ScreenLog() {
		super("ScreenLog", new ColumnBuilder(3)
				.add("Time", "INTEGER NOT NULL")
				.add("Duration", "INTEGER NOT NULL")
				.get());
		
		Network.AddHandler(onSendDone);
	}
	
	/**
	 * ScreenLog 테이블에 새 엔티티 삽입
	 * @param Time 화면 상태가 변화한 시간
	 * @param State 화면의 상태(true:켜짐, false:꺼짐)
	 */
	public void Insert(long Time, int Duration)
	{
		SQLiteDatabase db = DB.getInstance().getWritableDatabase();
		ContentValues values = new ContentValues(2);
		values.put(super.columns[0].Name, Time);
		values.put(super.columns[1].Name, Duration);
		db.insert(super.tableName, null, values);
		db.close();
	}
	
	/**
	 * ScreenLog의 모든 엔티티 반환
	 * @return
	 */
	public ScreenLogEntity[] SelectAll()
	{
		ArrayList<ScreenLogEntity> logs = new ArrayList<ScreenLogEntity>();
		String columns[] = { super.columns[0].Name, super.columns[1].Name };
		
		SQLiteDatabase db = DB.getInstance().getReadableDatabase();
		Cursor cursor = db.query(super.tableName, columns, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do logs.add( new ScreenLogEntity(
					cursor.getLong(0),
					cursor.getInt(1) ) );
			while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		return logs.toArray(new ScreenLogEntity[logs.size()]); 
	}

	/**
	 * Network 작업이 실행되고나면 호출되는 콜백 메서드
	 * 어차피 싱글톤 객체라서 ScreenLog 객체는 영원히 사라지지 않으므로, HandlerLeak 경고 Suppress
	 */
	private WeakReferencedHandler<ScreenLog> onSendDone = new WeakReferencedHandler<ScreenLog>(this) {
		@Override
		public void handleMessage(ScreenLog instance, Message msg) {
			switch (msg.what)
			{
			case Network.NETWORK_SUCCEEDED:
				SQLiteDatabase db = DB.getInstance().getWritableDatabase();
				db.delete(tableName, null, null);
				db.close();
				break;
			}
		}
	};
	
	
	
	// Static subclass&method to support singleton pattern
	/**
	 * Bill Pugh 싱글톤 패턴 지원용 클래스
	 * @author Hyeon
	 */
 	private static class SingletonHolder
	{
		public static final ScreenLog instance = new ScreenLog();
	}
	
	/**
	 * ScreenLog 클래스의 유일한 인스턴스를 반환한다.
	 * @return
	 */
	public static ScreenLog getInstance()
	{
		return SingletonHolder.instance;
	}
}
