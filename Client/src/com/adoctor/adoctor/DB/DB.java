// TODO Thread Unsafe (DB)

package com.adoctor.adoctor.DB;


import com.adoctor.adoctor.App;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 데이터베이스 추상화
 * @author Hyeon
 */
public class DB extends SQLiteOpenHelper {
	// Static members
	static final String DB_NAME = "Adoctor.db";
	static final int DB_VERSION = 1;
	
	static final Table[] tables = { ScreenLog.getInstance() };
	
	

	// Non-static methods
	/**
	 * DB 클래스 생성자. 외부로부터의 DB 클래스 인스턴스화가 금지되어있음
	 */
	private DB() {
		super(App.getContext(), DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for(Table table : tables) table.onCreate(db);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for(Table table : tables) 
		{
			db.execSQL("DROP TABLE IF EXISTS " + table.tableName);
			table.onCreate(db);
		}
	}
	
	
	
	// Static subclass&method to support singleton pattern
	/**
	 * Bill Pugh 싱글톤 패턴 지원용 클래스
	 * @author Hyeon
	 */
	private static class SingletonHolder {
		public static final DB instance = new DB();
	}
	
	/**
	 * DB 클래스의 유일한 인스턴스를 반환한다
	 * @return DB 클래스 인스턴스
	 */
	public static DB getInstance()
	{
		return SingletonHolder.instance;
	}
}