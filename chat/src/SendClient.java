import java.io.*;
import java.net.*;
import java.util.*;

public class SendClient implements Runnable{
	DatagramSocket sendSocket, receiveSocket;
	DatagramPacket sendPack,receivePack;
	private InetAddress sendIP;
	private int sendPort,receivePort;
	private byte inBuf[],outBuf[];
	public static final int buf = 1024;
	
	public static void main(String[] args){
		if(args.length==0)
				return ;
		String ip = args[0];
		int sendPort = Integer.parseInt(args[1]);
		int receivePort = Integer.parseInt(args[2]);
		
		// 创建通讯类，并启动线程
		// 创建的新的线程用于不停的接收信息，如果有新的信息来到，则显示在控制台上
		// 主线程用于接收输入
		SendClient sc = new SendClient(ip,sendPort,receivePort);
		(new Thread(sc)).start();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("press the 'enter' to send message:");
		while(true){
			//System.out.print("write in:");
			String msg="";
			try {
				msg = br.readLine();
				if(msg.equals("exit"))//貌似还是退出不了，还是直接用ctrl+c退出
					break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sc.sendMessage(msg);
		}

	}
	
	
	
	public SendClient() {
		super();
		try {
			this.sendIP = InetAddress.getByName("127.0.0.1");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sendPort = 12345;
		this.receivePort = 54321;
		this.inBuf = new byte[1024];
	}

	// 构造函数
	public SendClient(String sendIP, int sendPort, int receivePort) {
		super();
		try {
			this.sendIP = InetAddress.getByName(sendIP);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.sendPort = sendPort;
		this.receivePort = receivePort;
		this.inBuf = new byte[1024];
	}


	
	// 创建的新的线程用于不停的接收信息，如果有新的信息来到，则显示在控制台上
	@Override
	public void run() {
		String msg ;
		// TODO Auto-generated method stub
				try {
					this.receiveSocket = new DatagramSocket(receivePort);
					this.receivePack = new DatagramPacket(inBuf,1024);
				} catch (SocketException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				while(true)
				{
					try {
						receiveSocket.receive(receivePack);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					msg = new String(receivePack.getData(),0,receivePack.getLength());
					System.out.println("\nreceive message:"+msg);
						
				}
			}
	public void sendMessage(String msg){
		this.outBuf = msg.getBytes();
		this.sendPack = new DatagramPacket(this.outBuf,this.outBuf.length,this.sendIP,this.sendPort);
		try {
			this.sendSocket = new DatagramSocket();
			sendSocket.send(sendPack);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
	}
	// 从控制台接受三个参数
	// 第一个参数为需要连接的主机的IP
	// 第二个参数为向主机发送的端口号
	// 第三个参数为本机接受信息的端口号
	

	
}



