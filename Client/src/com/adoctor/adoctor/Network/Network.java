package com.adoctor.adoctor.Network;

import java.net.Socket;
import java.util.Vector;

import org.msgpack.MessagePack;
import org.msgpack.packer.BufferPacker;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import com.adoctor.adoctor.DB.DB;
import com.adoctor.adoctor.DB.ScreenLog;
import com.adoctor.adoctor.DB.ScreenLogEntity;
import com.adoctor.adoctor.pref.Preference;
import com.adoctor.adoctor.pref.PreferenceData;

public class Network extends AsyncTask<Void, Void, Integer> {
	
	public static final int NETWORK_SUCCEEDED = 0;
	public static final int NETWORK_FAILED = 1;
	
	private static Vector<Handler> HandlerList = new Vector<Handler>(2);
	
	// TODO 하드코딩 수정 (네트워크 설정)
	private static final String host = "uriel.upnl.org";
	private static final int port = 52301;
	
	private PreferenceData pref;
	private ScreenLogEntity[] logs;
	private Exception exception;
	
	/**
	 * 비동기 실행이 끝나면 실행될 콜백함수들의 목록에 handler 추가
	 * @param handler 추가할 콜백
	 */
	public static void AddHandler(Handler handler)
	{
		HandlerList.add(handler);
	}
	
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
	protected Integer doInBackground(Void... params) {
		try {
			MessagePack msgpack = new MessagePack();
			BufferPacker packer = msgpack.createBufferPacker();
			
			packer.writeMapBegin(2);
			{
				packer.write("version");
				packer.write(DB.DB_VERSION);
				
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
			
			// TODO 실행안됨
			Socket socket = new Socket(host, port);
			try {
				socket.getOutputStream().write(bytes);
			} finally {
				socket.close();
			}
			
			return NETWORK_SUCCEEDED;
		} catch (Exception e) {
			exception = e;
			return NETWORK_FAILED;
		}
	}

	/**
	 * 네트워크 작업이 성공적으로 끝났을 경우, 로컬DB의 내용을 삭제
	 */
	@Override
	protected void onPostExecute(Integer succeeded) {
		for(Handler handler : HandlerList)
		{
			Message msg = handler.obtainMessage(succeeded, exception);
			handler.sendMessage(msg);
		}
	}
}