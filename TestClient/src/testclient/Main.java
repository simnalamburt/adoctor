package testclient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * 프로그램 진입점
 */
public class Main {

	// TODO : 하드코딩 (서버 설정)
	private static String host = "uriel.upnl.org";
	private static int port = 52301;
	private static final int msglen = 1024;
	private static final String encoding = "UTF-8";

	/**
	 * 프로그램 진입점
	 * 
	 * @param args
	 *            커맨드라인 입력
	 *            -h, -host [hostname] : 호스트이름 설정. 기본값은 "uriel.upnl.org"
	 *            -p, -port [number] : 포트번호 변경. 기본값은 52301 
	 */
	public static void main(String[] args)
	{
		JSONObject userdata;
		userdata = new JSONObject();
		userdata.put("age", "20");
		userdata.put("sex", "male");
		JSONArray userarray = new JSONArray();
		userarray.add(userdata);
		
		// 커맨드라인 입력 파싱
		Options options = new Options();
		options.addOption("h", "host", true, "host name");
		options.addOption("p", "port", true, "port number");
		try
		{
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("h")) host = cmd.getOptionValue("h");
			if (cmd.hasOption("p")) port = Integer.parseInt(cmd.getOptionValue("p"));
		}
		catch (ParseException e)
		{
			System.out.print("△ 커맨드라인 입력 파싱 실패");
			String msg = e.getLocalizedMessage();
			if (msg == null) System.out.println();
			else System.out.println(" (" + msg + ")");
		}
		System.out.println("○ 호스트 : " + host);
		System.out.println("○ 포트 : " + port);

		// 서버와 통신
		try (Socket socket = new Socket(host, port);
			Scanner console = new Scanner(System.in))
		{
			OutputStream writer = socket.getOutputStream();
			InputStream reader = socket.getInputStream();

			byte[] buffer = new byte[msglen];
			System.out.println("● 아무것도 입력하지 않은 채 엔터를 누르면 접속이 종료됩니다");
			while (true)
			{
				System.out.print("전송 : ");
				String input = console.nextLine();
				if (input.equals("")) break;
				writer.write(userarray.toString().getBytes(encoding));
				//writer.write(input.getBytes(encoding));

				int len = reader.read(buffer);
				System.out.println("응답 : " + new String(buffer, 0, len, encoding));
			}
		}
		catch (UnknownHostException e)
		{
			System.out.print("△ 알 수 없는 Host Name \"" + host + "\"입니다.");
			String msg = e.getLocalizedMessage();
			if (msg == null) System.out.println();
			else System.out.println(" (" + msg + ")");
			try { System.in.read(); } catch (IOException _) { }
		}
		catch (IOException e)
		{
			System.out.print("△ 네트워크 I/O 도중 예외가 발생했습니다");
			String msg = e.getLocalizedMessage();
			if (msg == null) System.out.println();
			else System.out.println(" (" + msg + ")");
			try { System.in.read(); } catch (IOException _) { }
		}
	}
}