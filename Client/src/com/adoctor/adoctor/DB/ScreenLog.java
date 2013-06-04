package com.adoctor.adoctor.DB;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.template.builder.JavassistTemplateBuilder.JavassistTemplate;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.adoctor.adoctor.App;

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
	 * ScreenLog 테이블의 내용을 모두 서버로 보내고, 테이블을 비움 
	 */
	public void Flush()
	{
		new ScreenLogSender().execute();
	}
	
	/**
	 * 네트워크 Task 정의 클래스
	 * @author Hyeon
	 */
	private class ScreenLogSender extends AsyncTask<Void, Void, Boolean> {
		
		// TODO 하드코딩 수정 (네트워크 설정)
		private static final String host = "uriel.upnl.org";
		private static final int port = 52301;
		private static final int msglen = 1024;
		private static final String encoding = "UTF-8";
		
		private ScreenLogEntity[] logs;
		private String reply;
		
		/**
		 * DB의 내용을 받아옴. UI 스레드에서 실행됨
		 */
		@Override
		protected void onPreExecute() {
			logs = SelectAll();
		}

		/**
		 * 네트워크 I/O 수행. 별도 스레드에서 실행됨
		 */
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Socket socket = new Socket(host, port);
				MessagePack msgpack = new MessagePack();
				byte[] bytes = msgpack.write(logs);
				socket.getOutputStream().write(bytes);
				
				byte[] buffer = new byte[msglen];
				int len = socket.getInputStream().read(buffer);
				reply = new String(buffer, 0, len, encoding);
				
				socket.close();
				// TODO 수정
				return false;
			} catch (UnknownHostException e) {
				reply = "알 수 없는 Host Name입니다";
				return false;
			} catch (IOException e) {
				reply = "I/O 작업도중 예외가 발생했습니다 ( " + e.getLocalizedMessage() + " )";
				return false;
			} catch (Exception e) {
				reply = "Unhandled Exception 발생 ( " + e.getLocalizedMessage() + " )";
				return false;
			}
		}

		/**
		 * 네트워크 작업이 성공적으로 끝났을 경우, 로컬DB의 내용을 삭제
		 */
		@Override
		protected void onPostExecute(Boolean result) {
			if (result)
			{
				SQLiteDatabase db = DB.getInstance().getWritableDatabase();
				db.delete(tableName, null, null);
				db.close();
			}
			Toast.makeText(App.getContext(), reply, Toast.LENGTH_LONG).show();
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
