import java.net.*;
import java.nio.ByteBuffer;
import java.io.*;
import java.util.*;

class StftpServerWorker extends Thread
{
	private DatagramPacket req;
	private DatagramSocket ds;
	private static final int REQ = 3;
	private static final int OK = 1;
	private static final int DATA = 2;
	private static final int ACK = 42;
	private static final int NOTOK = 0;
	private static int Total_packet = 0;

	private static void storeLong(byte[] array, int off, long val)
	{

		array[off + 0] = (byte) ((val & 0xff00000000000000L) >> 56);
		array[off + 1] = (byte) ((val & 0x00ff000000000000L) >> 48);
		array[off + 2] = (byte) ((val & 0x0000ff0000000000L) >> 40);
		array[off + 3] = (byte) ((val & 0x000000ff00000000L) >> 32);
		array[off + 4] = (byte) ((val & 0x00000000ff000000L) >> 24);
		array[off + 5] = (byte) ((val & 0x0000000000ff0000L) >> 16);
		array[off + 6] = (byte) ((val & 0x000000000000ff00L) >> 8);
		array[off + 7] = (byte) ((val & 0x00000000000000ffL));
		return;
	}

	private static long extractLong(byte[] array, int off)
	{
		long a = array[off + 0] & 0xff;
		long b = array[off + 1] & 0xff;
		long c = array[off + 2] & 0xff;
		long d = array[off + 3] & 0xff;
		long e = array[off + 4] & 0xff;
		long f = array[off + 5] & 0xff;
		long g = array[off + 6] & 0xff;
		long h = array[off + 7] & 0xff;
		return (a << 56 | b << 48 | c << 40 | d << 32 | e << 24 | f << 16
				| g << 8 | h);
	}

	/**
	 * @param filename
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private void sendfile(String filename) throws IOException
	{
		ds = new DatagramSocket();
		ds.connect(req.getSocketAddress());
		FileInputStream input;
		try
		{
			File file = new File(filename);
			input = new FileInputStream(file);
			RandomAccessFile rf = null;
			byte[] Data = null;
			rf = new RandomAccessFile(file, "r");
			long filelength = rf.length();
			long num_packet = filelength / 1472;
			String info = String.valueOf(filelength);
			
			
			Data = new byte[9];// SET OK PACKET WILL HAVE 9 BYTES
			Data[0] = OK;
			storeLong(Data, 1, file.length());
			DatagramPacket dp = new DatagramPacket(Data, Data.length);
			ds.send(dp);
			ds.setSoTimeout(1000);
			
			
			int temp;
			int count = 0;
			
			Data = new byte[1463];
			while ((temp = input.read(Data)) != -1 || count > 1)// RE- FORMAT THE DATA LIKE [ DATA, OFFSET*8,DATA....]
			{
				byte[] data = new byte[1463 + 9];
				data[0] = DATA;
				storeLong(data, 1, 0);
				for (int i = 0; i < temp; i++)
				{
					data[i + 9] = Data[i];
				}
				dp = new DatagramPacket(data, temp + 9);
				int packet_length = temp + 9;

				Total_packet = Total_packet + packet_length;
				System.out.println("Sending" + " to" + req.getSocketAddress()
						+ " -- " + " PACKET: " + packet_length + " TOTAL-NUM: "
						+ Total_packet);
				boolean send = false;
				while (!send)// THS FOUNCATION WILL CHIECK THE SIZE OF PACKET" 1472" OR LESS 1472
				{

					ds.send(dp);
					byte[] buf = new byte[1472];
					DatagramPacket p = new DatagramPacket(buf, 1472);
					try
					{
						ds.receive(p);
						if (packet_length == 1472)// THIS MEANS THE NORMAL PACKET [DATA PACKET]
						{
							if (p.getData()[0] == ACK)
							{
								send = true;
								count = 0;
							} else
							{
								count++;
							}

						}
						if (packet_length < 1472)// THIS MEANS THE PAKECT SIZE LESS THEN 1472 AND THIS IS A LAST PACKET
						{
							send = false;// SET THE "WHILE" NOT RUN AND PRINT "TRANSFAT DONE"
							input.close();
							System.out.println("Sending---- finished!");
							System.out.println(num_packet + "//" + info);
							return;
						}
					} catch (Exception e)
					{
						System.err.println("Exception: " + e);
					}
				}
			}
		} catch (FileNotFoundException e)// IF GET FILE DATA FAILED, IT WILL SEEND "NOTOK" PACKET TO CLIENT 
		{
			byte[] nofind = new byte[1];
			nofind[0] = NOTOK;
			DatagramPacket dp = new DatagramPacket(nofind, 1);
			ds.send(dp);
		}
		return;
	}
	/**
	 * @param filename
	 * @throws IOException
	 */
	public void run()
	{
		byte[] data = req.getData();
		if (((int) data[0]) == REQ)
		{
			String temp = new String(data, 1, req.getLength() - 1);
			try
			{
				sendfile(temp);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return;
	}

	public StftpServerWorker(DatagramPacket req)
	{
		this.req = req;
	}
}

public class StftpServer
{
	public void startServer()
	{
		try
		{
			DatagramSocket ds = new DatagramSocket(44440);
			System.out.println("TtftpServer Online: "
					+ InetAddress.getLocalHost().getHostAddress() + ":"
					+ ds.getLocalPort());

			for (;;)
			{
				byte[] buf = new byte[1472];
				DatagramPacket p = new DatagramPacket(buf, 1472);
				ds.receive(p);

				StftpServerWorker worker = new StftpServerWorker(p);
				worker.start();
			}
		} catch (Exception e)
		{
			System.err.println("Exception: " + e);
		}

		return;
	}

	public static void main(String args[])
	{
		StftpServer d = new StftpServer();
		d.startServer();
	}
}
