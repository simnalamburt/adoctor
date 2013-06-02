package com.adoctor.adoctor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 데이터베이스 추상화
 * @author Hyeon
 *
 */
public class DB extends SQLiteOpenHelper {
	// Static members
	public static final String DB_NAME = "Adoctor.db";
	public static final int DB_VERSION = 1;
	
	
	
	// Static subclass&method to support singleton pattern
	/**
	 * Bill Pugh 싱글톤 패턴 지원용 클래스
	 * @author Hyeon
	 */
	private static class SingletonHolder {
		public static final DB INSTANCE = new DB();
	}
	
	
	/**
	 * DB 클래스의 유일한 인스턴스를 반환한다
	 * @return DB 클래스 인스턴스
	 */
	public static DB getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	

	// Non-static members
	/**
	 * 외부로부터의 DB 클래스 인스턴스화 금지
	 */
	private DB() {
		super(App.getContext(), DB_NAME, null, DB_VERSION);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 구현하기 (onCreate)
		db.execSQL("CREATE TABLE IF NOT EXISTS");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO 구현하기 (onUpgrade);
		db.execSQL("");
	}
}

// TODO Thread Safety 확인하기