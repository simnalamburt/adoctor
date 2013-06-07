package com.adoctor.adoctor.DB;

import java.net.Socket;
import java.util.ArrayList;

import org.msgpack.MessagePack;
import org.msgpack.packer.BufferPacker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.adoctor.adoctor.App;
import com.adoctor.adoctor.pref.Preference;
import com.adoctor.adoctor.pref.PreferenceData;

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
		values.put(super.columns[0].Name, Time);
		values.put(super.columns[1].Name, State.toInt());
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
					ScreenState.fromInt(cursor.getInt(1)) ) );
			while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		
		return logs.toArray(new ScreenLogEntity[logs.size()]); 
	}

	/**
	 * ScreenLog 테이블의 내용을 모두 서버로 보내고, 테이블을 비움 
	 */
	public void Flush()
	{
		new ScreenLogSender().execute();
	}
	
	private class ScreenLogSender extends AsyncTask<Void, Void, Boolean> {
		
		// TODO 하드코딩 수정 (네트워크 설정)
		private static final String host = "uriel.upnl.org";
		private static final int port = 52301;
		
		private PreferenceData pref;
		private ScreenLogEntity[] logs;
		private Exception exception;
		
		/**
		 * DB의 내용을 받아옴. UI 스레드에서 실행됨
		 */
		@Override
		protected void onPreExecute() {
			pref = Preference.getPref();
			logs = ScreenLog.getInstance().SelectAll();
		}

		/**
		 * 네트워크 I/O 수행. 별도 스레드에서 실행됨
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				// TODO 순서 최적화
				Socket socket = new Socket(host, port);
				try {
					MessagePack msgpack = new MessagePack();
					BufferPacker packer = msgpack.createBufferPacker();
					
					packer.writeMapBegin(2);
					{
						packer.write("version");
						packer.write(0);
						
						packer.write("data");
						packer.writeMapBegin(2);
						{
							packer.write("pref");
							packer.write(pref);
							
							packer.write("logs");
							packer.write(logs);
						}
						packer.writeMapEnd();
					}
					packer.writeMapEnd();
					
					byte[] bytes = packer.toByteArray();
					socket.getOutputStream().write(bytes);
				} finally {
					socket.close();
				}
				return true;
			} catch (Exception e) {
				exception = e;
				return false;
			}
		}

		/**
		 * 네트워크 작업이 성공적으로 끝났을 경우, 로컬DB의 내용을 삭제
		 */
		@Override
		protected void onPostExecute(Boolean succeeded) {
			if (succeeded) {
				SQLiteDatabase db = DB.getInstance().getWritableDatabase();
				db.delete(tableName, null, null);
				db.close();
			} else {
				Toast.makeText(App.getContext(), exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
			}
		}
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
