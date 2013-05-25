package testclient;

import java.io.*;
import java.net.*;

/**
 * 프로그램 진입점
 */
public class Main {

	/**
	 * 프로그램 진입점
	 * 
	 * @param args
	 *            프로그램 실행 인자
	 */

	// TODO : 서버에 따라 변경해야함
	private static final String host = "localhost";
	private static final int port = 52301;

	private static byte[] obuffer = new byte[1024];
	private static byte[] ibuffer = new byte[1024];

	public static void main(String[] args) {
		try (Socket sock = new Socket(host, port)) {
			OutputStream writer = sock.getOutputStream();
			InputStream reader = sock.getInputStream();

			while (true) {
				int len = System.in.read(obuffer);
				writer.write(obuffer, 0, len);

				len = reader.read(ibuffer);
				System.out.write(ibuffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

}
