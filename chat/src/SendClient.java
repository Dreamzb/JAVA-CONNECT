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
		
		// ����ͨѶ�࣬�������߳�
		// �������µ��߳����ڲ�ͣ�Ľ�����Ϣ��������µ���Ϣ����������ʾ�ڿ���̨��
		// ���߳����ڽ�������
		SendClient sc = new SendClient(ip,sendPort,receivePort);
		(new Thread(sc)).start();
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		System.out.println("press the 'enter' to send message:");
		while(true){
			//System.out.print("write in:");
			String msg="";
			try {
				msg = br.readLine();
				if(msg.equals("exit"))//ò�ƻ����˳����ˣ�����ֱ����ctrl+c�˳�
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

	// ���캯��
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


	
	// �������µ��߳����ڲ�ͣ�Ľ�����Ϣ��������µ���Ϣ����������ʾ�ڿ���̨��
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
	// �ӿ���̨������������
	// ��һ������Ϊ��Ҫ���ӵ�������IP
	// �ڶ�������Ϊ���������͵Ķ˿ں�
	// ����������Ϊ����������Ϣ�Ķ˿ں�
	

	
}



