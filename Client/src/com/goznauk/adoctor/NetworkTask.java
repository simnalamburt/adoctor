package com.goznauk.adoctor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class NetworkTask extends AsyncTask<Void, Void, String> {
	private Context context;

	public NetworkTask(Context context) {
		this.context = context;
	}

	@Override
	protected String doInBackground(Void... params) {
		String msg;
		try {
			Socket socket = new Socket("uriel.upnl.org", 52301);
			OutputStream writer = socket.getOutputStream();
			InputStream reader = socket.getInputStream();
			writer.write("안드로이드에서 보낸 메세지".getBytes("UTF-8"));
			byte[] buffer = new byte[1024];
			int len = reader.read(buffer);
			msg = new String(buffer, 0, len, "UTF-8");
			socket.close();
		} catch (UnknownHostException e) {
			msg = "△ 알 수 없는 Host Name입니다";
		} catch (IOException e) {
			msg = "△ 네트워크 I/O 도중 예외가 발생했습니다 ( " + e.getMessage() + " )";
		}
		return msg;
	}

	@Override
	protected void onPostExecute(String result) {
		Toast.makeText(context, result, Toast.LENGTH_LONG).show();
	}

}
