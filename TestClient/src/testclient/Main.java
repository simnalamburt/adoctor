package testclient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;
import org.msgpack.annotation.MessagePackOrdinalEnum;
import org.msgpack.packer.Packer;

/**
 * 프로그램 진입점
 */
public class Main {

	// TODO 하드코딩 (서버 설정)
	private static String host = "uriel.upnl.org";
	private static int port = 52301;

	private static final String escape = "/disconnect";
	
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
		ParseCmd(args);
		Send();
	}
	
	/**
	 * 커맨드라인 입력 파싱
	 * @param Args 커맨드라인 입력
	 */
	public static void ParseCmd(String[] Args)
	{
		Options options = new Options();
		options.addOption("h", "host", true, "host name");
		options.addOption("p", "port", true, "port number");
		try
		{
			CommandLineParser parser = new GnuParser();
			CommandLine cmd = parser.parse(options, Args);

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
	}
	
	/**
	 * 통신
	 */
	public static void Send()
	{
		try (Socket socket = new Socket(host, port);
			Scanner console = new Scanner(System.in))
		{
			OutputStream writer = socket.getOutputStream();
			System.out.println("● '"+escape+"'를 입력하면 접속이 종료됩니다");
			do
			{
				MessagePack msgpack = new MessagePack();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				Packer packer = msgpack.createPacker(out);
				
				for(int i=0;i<3;++i) packer.write(new Entry());
				byte[] bytes = out.toByteArray();
				
				
				writer.write(bytes);
				System.out.println("전송 : " + bytes.toString());
			} while (!console.nextLine().equals(escape));
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

@MessagePackOrdinalEnum
enum State { Off, On }

@Message
class Entry
{
	public long time;
	public State state;
	
	private static State lastState = State.On;
	Entry()
	{
		time = System.currentTimeMillis();
		state = (lastState == State.On ? (lastState = State.Off) : (lastState = State.On));
	}
}