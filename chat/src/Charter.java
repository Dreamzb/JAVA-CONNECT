import java.io.*;
import java.net.*;
import java.util.*;

//import Charter.Peer;



public class Charter {
	//public static LinkedList<InetAddress> list;
	public static int port = 50000;
	//This ought to be with the peer class

	public static LinkedList<Peer> peers;
	static Peer addpeers ;
	public static void main(String[] args) throws Exception {
		 peers = new LinkedList<Peer>();
		
		new Thread(new receive(new DatagramSocket(port))).start(); // Server
		new Thread(new Send(new DatagramSocket())).start(); // Client
		//Sending does not need to be in another thread.
		String Ip = null;
		//list = new LinkedList<InetAddress>();
		
		
		//add new Address
		//Adding a new IP address is done with a new command 'NEW 130.217.0.0' or similar
		int i=0;
		if(args.length==0){ //You should not need this if statement
			String DIp = "255.255.255.255"; //You do not need the Broadcast IP address,  this should be adding an IP read from the screen in the new command 
			InetAddress toAdd = InetAddress.getByName(DIp);
			addpeers= new Peer(toAdd);
			peers.add(addpeers);
			System.out.println(peers.element().otherIP()+"general Ip");
		}
		else{
			Ip = args[i];
			InetAddress toAdd =InetAddress.getByName(Ip);
			addpeers = new Peer(toAdd);
			peers.add(addpeers);
			i++;
			System.out.println(peers.element().otherIP()+"else loop general Ip");
		}
		String LocalIP = InetAddress.getLocalHost().getHostAddress().toString();
		System.out.println("This Comupter's IP is" + LocalIP);
		printIP();
	}
	
	public static void printIP(){ //This method is a good base to modify for sending data
		ListIterator upto =peers.listIterator(0);		 
		while (upto.hasNext()) {
			 
			Peer peer = (Peer)upto.next();
			InetAddress print_1111=peer.otherIP();
			 System.out.println(peers.element());
			 System.out.println(peers.element().otherIP());
	            /*if(e2.equals(e1))
	            	upto.remove();
	            else
	            	e1 = e2;*/
	           
		}
	}
	

	static class Send implements Runnable { //This does not need to be a thread, it can be done in the Main method of the program
		private DatagramPacket dPacket;
		private DatagramSocket dSocket;
		//Peer addpeers ;
		public Send(DatagramSocket dSocket) {
			this.dSocket = dSocket;
		}

		public void run() {
			
			try {
				String IP = InetAddress.getLocalHost().getHostAddress();
				System.out.println(IP+"send IP ");
				BufferedReader bufferedReader = new BufferedReader(
						new InputStreamReader(System.in));
				String string;
				while ((string = bufferedReader.readLine()) != null) {
					if (string.equals("New")) {
						peers.clear();
						
						
						InetAddress toAdd = InetAddress.getByName(IP);
						System.out.println(toAdd+"send IP =====");
						
					addpeers = new Peer(toAdd);
					peers.add(addpeers);
					System.out.println(peers.element().otherIP()+"send IP ");
                       //list.add(InetAddress.getByName(IP));
					}
					byte[] buf = new byte[1024];
					buf = string.getBytes();
					dPacket = new DatagramPacket(buf, buf.length,Peer.otherIP(), port);
							//list.element() 
			
					dSocket.send(dPacket);

				}
			} catch (Exception e) {
				System.err.println("error");
				e.printStackTrace();
			}
		}

	}

	 static class receive implements Runnable { //This looks OK
		
		private DatagramSocket dSocket;
		//Peer addpeers ;
		public receive(DatagramSocket dSocket) {
			this.dSocket = dSocket;
		}

		public void run() {
			while (true) {
				DatagramPacket dPacket;
				byte[] buf = new byte[1024];
				dPacket = new DatagramPacket(buf, buf.length);
				try {
					dSocket.receive(dPacket);
					String OtherIP = dPacket.getAddress().getHostAddress();
					addpeers = new Peer(InetAddress.getByName(OtherIP));
					peers.add(addpeers);
					System.out.println(peers.element().otherIP()+"receive ip");
					//list.add(InetAddress.getByName(OtherIP));
					InetAddress newDNS = InetAddress.getByName(OtherIP);
					String data = new String(dPacket.getData(), 0, dPacket.getLength());
					System.out.println(newDNS.getHostName() + " IP:" + OtherIP + ":" + data);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}
	 static class Peer //The list of Peers needs to be here
	{
		private static InetAddress Ipaddress; //This should not be static, each peer has its own IP address
		
		///private static Peer peer;
		
		private Peer(InetAddress IP){
			Ipaddress = IP;
			//peers.add(this);
		}
		
		public static InetAddress otherIP()
		{
			return Ipaddress;
		}

		public static LinkedList<Peer> getPeerList()
		{
			return peers;
		}
		
		
	}
	 

		

}

