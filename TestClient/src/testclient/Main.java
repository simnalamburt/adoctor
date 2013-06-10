package testclient;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.msgpack.MessagePack;
import org.msgpack.MessagePackable;
import org.msgpack.packer.Packer;
import org.msgpack.unpacker.Unpacker;

/**
 * 프로그램 진입점
 */
public class Main {

	// TODO 하드코딩 (서버 설정)
	private static String host = "uriel.upnl.org";
	private static int port = 52301;
	
	private static String escape = "exit";

	/**
	 * 프로그램 진입점
	 * 
	 * @param args
	 *            커맨드라인 입력 -h, -host [hostname] : 호스트이름 설정. 기본값은
	 *            "uriel.upnl.org" -p, -port [number] : 포트번호 변경. 기본값은 52301
	 */
	public static void main(String[] args) {
		try {
			ParseCmd(args);
			Send();
		} catch (Exception e) {
			System.out.println("△ " + e.getLocalizedMessage());
		}
	}

	/**
	 * 커맨드라인 입력 파싱
	 * 
	 * @param Args
	 *            커맨드라인 입력
	 * @throws ParseException
	 *             커맨드라인 파싱이 실패했을 경우 발생
	 */
	public static void ParseCmd(String[] Args) throws ParseException {
		Options options = new Options();
		options.addOption("h", "host", true, "host name");
		options.addOption("p", "port", true, "port number");

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(options, Args);

		if (cmd.hasOption("h"))
			host = cmd.getOptionValue("h");
		if (cmd.hasOption("p"))
			port = Integer.parseInt(cmd.getOptionValue("p"));
	}

	/**
	 * 통신
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public static void Send() throws UnknownHostException, IOException {
		System.out.println("○ 호스트 : " + host);
		System.out.println("○ 포트 : " + port);
		System.out.println("● '"+escape+"' 입력시 프로그램 종료");

		Dummy dummy = new Dummy();
		try (Scanner scanner = new Scanner(System.in)) {
			while (!scanner.nextLine().equals(escape)) {
				MessagePack msgpack = new MessagePack();
				byte[] bytes = msgpack.write(dummy);
				
				try (Socket socket = new Socket(host, port)) {
					socket.getOutputStream().write(bytes);
				}
				
				System.out.print("전송 : " + bytes.toString() + "   ");
			}
		}
	}
}

/**
 * 더미 입력 클래스
 * @author Hyeon
 */
class Dummy implements MessagePackable
{
	public static Random rand = new Random();
	
	/**
	 * 시리얼라이저
	 */
	@Override
	public void writeTo(Packer packer) throws IOException {
		packer.writeMapBegin(2);
		{
			packer.write("version");
			packer.write(0);
	
			packer.write("data");
			packer.writeMapBegin(2);
			{
				packer.write("pref");
				packer.writeMapBegin(3);
				{
					packer.write("age");
					packer.write(20);
					packer.write("job");
					packer.write(4);
					packer.write("sex");
					packer.write(0);
				}
				packer.writeMapEnd();
		
				packer.write("logs");
				int count = rand.nextInt(3);
				packer.writeArrayBegin(count);
				for (int i = 0; i < count; ++i) {
					packer.writeArrayBegin(2);
					packer.write(System.currentTimeMillis());
					packer.write(rand.nextInt(10000));
					packer.writeArrayEnd();
				}
				packer.writeArrayEnd();
			}
			packer.writeMapEnd();
		}
		packer.writeMapEnd();
	}
	/**
	 * 디시리얼라이저
	 */
	@Override
	public void readFrom(Unpacker u) throws IOException { }
}
