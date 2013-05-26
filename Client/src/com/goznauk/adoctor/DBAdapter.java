package com.goznauk.adoctor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * DBMS를 돕는 utility 클래스
 * @author 훈존
 * 
 */
public class DBAdapter {
	private DatabaseHelper mHelper; // SQLiteOpenHelper : db create, open,
									// version upgrade
	private SQLiteDatabase mDb = null; // SQLiteDatabase : insert, query,
										// delete, update

	private static final String DATABASE_NAME = "scrlog.db"; // db 이름
	private static final int DATABASE_VERSION = 1; // db버젼
	private static String SQL_TABLE_CREATE;

	private static String TABLE_NAME;

	private final Context mCxt;

	/**
	 * DB를 open, update, drop 시키는 역할의 SQLiteOpenHelper 클래스
	 * @author 훈존
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		// Helper의 기능인 create, open, version upgrade를 진행

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(SQL_TABLE_CREATE);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	// Main에서 context와 sql명, tableName을 전해주면서 new할 경우 호출된다.

	/**
	 * DBAdapter 생성자
	 * DBAdapter를 받을 context와, 만들 table의 SQL 생성문, 이름을 받아 DBAdapter객체를 만든다. 
	 * @param cxt context
	 * @param sql SQL 생성문, SQL_CREATE_SCRLOG은 SCRLOG 생성문
	 * @param tableName SQL TABLE NAME
	 */
	public DBAdapter(Context cxt, String tableName) {
		this.mCxt = cxt;
		TABLE_NAME = tableName;
		// DB 생성문
		/**
		 * SCRLOG DB의 생성문
		 * _ID, TIME, SCREENSTATE
		 */
		SQL_TABLE_CREATE = "create table if not exists " + TABLE_NAME + 
				" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
				" TIME INT NOT NULL," + 
				" SCREENSTATE TEXT" + ")";
	}
	
	/**
	 * 외부에서 DB를 사용하겠다고 요청이 들어오면 Helper를 이용해서 DB를 open하고 자신의 클래스를 리턴
	 * @return DBAdapter
	 * @throws SQLException
	 */
	public DBAdapter open() throws SQLException {
		// 
		mHelper = new DatabaseHelper(mCxt); // SQLiteOpenHelper에게 context를 넘겨준다.
		mDb = mHelper.getWritableDatabase();
		return this;
	}

	/**
	 * DBAdapter 닫기
	 */
	public void close() {
		mHelper.close();
	}

	// insert, delete, select, update의 db기본 기능 수행
	
	/**
	 * ContentValues 객체를 받아 DB에 insert 한다.
	 * @param values
	 * @return
	 */
	public long insertTable(ContentValues values) {
		return mDb.insert(TABLE_NAME, null, values);
	}

	/**
	 * 
	 * @param pkColumn
	 * @param pkData
	 * @return
	 */
	public boolean deleteTable(String pkColumn, long pkData) {
		return mDb.delete(TABLE_NAME, pkColumn + "=" + pkData, null) > 0;
	}

	/**
	 * 
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return
	 */
	public Cursor selectTable(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return mDb.query(TABLE_NAME, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

	/**
	 * 
	 * @param values
	 * @param pkColumn
	 * @param pkData
	 * @return
	 */
	public boolean updateTable(ContentValues values, String pkColumn,
			long pkData) {
		return mDb.update(TABLE_NAME, values, pkColumn + "=" + pkData, null) > 0;
	}
	
	/**
	 * query를 받아 DB에 적용한다.
	 * @param query
	 */
	public void query(String query) {
		mDb.execSQL(query);
	}
}