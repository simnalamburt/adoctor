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

	// 여러 테이블을 이용할 경우 아래처럼 테이블을 생성하기 위한 sql만 담아서 범용적으로
	// DBAdapter 클래스를 사용하는게 좋다.
	public static final String SQL_CREATE_SCRLOG = "create table if not exists " + TABLE_NAME + 
													" (_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + 
													" TIME TEXT NOT NULL," + 
													" SCREENSTATE TEXT" + ")";

	private final Context mCxt;

	// DB를 open, update, drop 시키는 역할의 SQLiteOpenHelper 클래스
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
			// TODO Auto-generated method stub
			db.execSQL(SQL_TABLE_CREATE);
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			super.onOpen(db);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}

	}

	// Main에서 context와 sql명, tableName을 전해주면서 new할 경우 호출된다.

	/**
	 * DBAdapter를 이용하고자 하는 곳에서 생성자에 table 생성 sql과 table명만 주고 이용해라.
	 * @param cxt
	 * @param sql
	 * @param tableName
	 */
	public DBAdapter(Context cxt, String sql, String tableName) {
		this.mCxt = cxt;
		SQL_TABLE_CREATE = sql;
		TABLE_NAME = tableName;
	}

	public DBAdapter open() throws SQLException {
		// 외부에서 db를 사용하겠다고 요청이 들어오면 Helper를 이용해서 db를 open하고
		// 자신의 클래스를 리턴
		mHelper = new DatabaseHelper(mCxt); // SQLiteOpenHelper에게 context를 넘겨준다.
		mDb = mHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mHelper.close();
	}

	// insert, delete, select, update의 db기본 기능 수행
	public long insertTable(ContentValues values) {
		return mDb.insert(TABLE_NAME, null, values);
	}

	public boolean deleteTable(String pkColumn, long pkData) {
		return mDb.delete(TABLE_NAME, pkColumn + "=" + pkData, null) > 0;
	}

	public Cursor selectTable(String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return mDb.query(TABLE_NAME, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

	public boolean updateTable(ContentValues values, String pkColumn,
			long pkData) {
		return mDb.update(TABLE_NAME, values, pkColumn + "=" + pkData, null) > 0;
	}
}
