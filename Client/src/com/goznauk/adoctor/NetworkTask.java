package com.goznauk.adoctor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

/**
 * 안드로이드에서 네트워킹 사용시 호출되는 AsynchTask
 * TODO : 작업중
 * @author Hyeon
 *
 */
public class NetworkTask extends AsyncTask<String, Void, String> {

	// TODO : 하드코딩 (서버 설정)
	private static String host = "uriel.upnl.org";
	private static int port = 52301;
	private static final int msglen = 1024;
	private static final String encoding = "UTF-8";

	private Context context;

	/**
	 * 클래스 생성자
	 * @param context 토스트 메세지가 뜰 Context
	 */
	public NetworkTask(Context context) {
		this.context = context;
	}

	/**
	 * 배경작업 정의. 독립 스레드에서 실행됨
	 */
	@Override
	protected String doInBackground(String... params) {
		String msg;
		try {
			Socket socket = new Socket(host, port);
			OutputStream writer = socket.getOutputStream();
			InputStream reader = socket.getInputStream();
			writer.write(params[0].getBytes(encoding));
			byte[] buffer = new byte[msglen];
			int len = reader.read(buffer);
			msg = new String(buffer, 0, len, encoding);
			socket.close();
		} catch (UnknownHostException e) {
			msg = "△ 알 수 없는 Host Name입니다";
		} catch (IOException e) {
			msg = "△ 네트워크 I/O 도중 예외가 발생했습니다 ( " + e.getMessage() + " )";
		}
		return msg;
	}

	/**
	 * 실행후 작업 정의. 메인스레드에서 실행됨
	 */
	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, result, Toast.LENGTH_LONG).show();
	}

}
