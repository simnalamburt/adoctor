// TODO Thread Unsafe (DB)

package com.adoctor.adoctor.DB;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * ScreenLog 테이블 싱글톤 클래스
 * @author Hyeon
 */
public class ScreenLog extends Table {
	// Non-static methods
	/**
	 * ScreenLog 클래스 생성자. 외부 클래스로부터의 인스턴스화가 금지되어있음.
	 */
	private ScreenLog() {
		super("ScreenLog", new ColumnBuilder(3)
				.add("ID", "INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT")
				.add("Time", "INTEGER NOT NULL")
				.add("State", "INTEGER NOT NULL").get());
	}
	
	/**
	 * ScreenLog 테이블에 새 엔티티 삽입
	 * @param Time 화면 상태가 변화한 시간
	 * @param State 화면의 상태(true:켜짐, false:꺼짐)
	 */
	public void Insert(long Time, ScreenState State)
	{
		SQLiteDatabase db = DB.getInstance().getWritableDatabase();
		ContentValues values = new ContentValues(2);
		values.put(super.columns[1].Name, Time);
		values.put(super.columns[2].Name, State.toInt());
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
		String columns[] = { super.columns[1].Name, super.columns[2].Name };
		
		SQLiteDatabase db = DB.getInstance().getReadableDatabase();
		Cursor cursor = db.query(super.tableName, columns, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do logs.add( new ScreenLogEntity(
					cursor.getLong(0),
					ScreenState.fromInt(cursor.getInt(1)) ) );
			while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		return logs.toArray(new ScreenLogEntity[logs.size()]); 
	}

	/**
	 * ScreenLog 테이블 비우기
	 */
	public void Flush()
	{
		SQLiteDatabase db = DB.getInstance().getWritableDatabase();
		db.delete(super.tableName, null, null);
		db.close();
	}

	
	
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
