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
import org.msgpack.packer.BufferPacker;

/**
 * 프로그램 진입점
 */
public class Main {

	// TODO 하드코딩 (서버 설정)
	private static String host = "uriel.upnl.org";
	private static int port = 52301;

	/**
	 * 프로그램 진입점
	 * 
	 * @param args
	 *            커맨드라인 입력 -h, -host [hostname] : 호스트이름 설정. 기본값은
	 *            "uriel.upnl.org" -p, -port [number] : 포트번호 변경. 기본값은 52301
	 */
	public static void main(String[] args) {
		ParseCmd(args);
		MainLoop();
	}

	/**
	 * 커맨드라인 입력 파싱
	 * 
	 * @param Args
	 *            커맨드라인 입력
	 * @throws ParseException
	 *             커맨드라인 파싱이 실패했을 경우 발생
	 */
	private static void ParseCmd(String[] Args) {
		Options options = new Options();
		options.addOption("h", "host", true, "host name");
		options.addOption("p", "port", true, "port number");

		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		try {
			cmd = parser.parse(options, Args);
		} catch (ParseException e) {
			System.out.println("△ 커맨드라인 파싱 실패, 기본값 사용");
			return;
		}

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
	public static void MainLoop() {
		System.out.println("○ 호스트 : " + host);
		System.out.println("○ 포트 : " + port);
		System.out.println("● 0 입력시 프로그램 종료");
		
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				int count = scanner.nextInt();
				if ( count <= 0 ) break;
				for(int i=0; i<count; ++i)
					new Thread(onSend).start();
			}
		}
	}
	
	/**
	 * 스레드 행동 정의
	 */
	private static Runnable onSend = new Runnable() {
		@Override
		public void run() {
			Random rand = new Random();
			MessagePack msgpack = new MessagePack();
			BufferPacker packer = msgpack.createBufferPacker();
			
			try {
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
						int count = 5 + rand.nextInt(5);
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
				
				byte[] bytes = packer.toByteArray();
				try (Socket socket = new Socket(host, port)) {
					socket.getOutputStream().write(bytes);
					System.out.println("전송성공 : " + bytes.toString());
				} catch (UnknownHostException e) {
					System.out.println("Host와의 연결에 실패 : " + e);
				} catch (IOException e) {
					System.out.println("네트워크 I/O 도중 예외발생 : " + e);
				}
			} catch (IOException e) {
				System.out.println("MsgPack 생성중 예외발생 : " + e);
			}
		}
	};
}
