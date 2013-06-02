package com.adoctor.adoctor;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * ScreenLog 테이블 추상화
 * @author Hyeon
 */
public class ScreenLog implements BaseColumns {
	// Static Member&Methods
	public static final String TABLE_NAME = "ScreenLog";
	public static final String COLUMN_NAME_TIME = "Time";
	public static final String COLUMN_NAME_STATE = "State";
	
	/**
	 * ScreenLog 테이블에 새 엔티티 삽입
	 * @param Time 화면 상태가 변화한 시간
	 * @param State 화면의 상태(true:켜짐, false:꺼짐)
	 */
	public static void Insert(long Time, ScreenState State)
	{
		SQLiteDatabase db = DB.getInstance().getWritableDatabase();
		
		ContentValues values = new ContentValues(2);
		values.put(COLUMN_NAME_TIME, Time);
		values.put(COLUMN_NAME_STATE, State.toInt());
		db.insert(TABLE_NAME, null, values);
		
		db.close();
	}
	
	/**
	 * ScreenLog의 모든 엔티티 반환
	 * @return
	 */
	public static ScreenLog[] SelectAll()
	{
		ArrayList<ScreenLog> logs = new ArrayList<ScreenLog>();
		String columns[] = { COLUMN_NAME_TIME, COLUMN_NAME_STATE };
		
		SQLiteDatabase db = DB.getInstance().getReadableDatabase();
		Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			do logs.add( new ScreenLog(
					cursor.getLong(0),
					ScreenState.fromInt(cursor.getInt(1)) ) );
			while (cursor.moveToNext());
		}
		
		cursor.close();
		db.close();
		
		return logs.toArray(new ScreenLog[logs.size()]); 
	}
	
	/**
	 * ScreenLog 테이블 비우기
	 */
	public static void Flush()
	{
		SQLiteDatabase db = DB.getInstance().getWritableDatabase();
		
		db.delete(TABLE_NAME, null, null);
		
		db.close();
	}
	
	
	
	// Non-static Member&Methods
	public long Time;
	public ScreenState State;
	
	/**
	 * ScreenLog 엔티티 인스턴스 생성
	 * @param Time 화면 상태가 변화한 시간
	 * @param State 화면의 상태(true:켜짐, false:꺼짐)
	 */
	public ScreenLog(long Time, ScreenState State)
	{
		this.Time = Time;
		this.State = State;
	}
}