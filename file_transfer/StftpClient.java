import java.net.*;
import java.io.*;
import java.util.*;

public class StftpClient
{
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

	public void startClient()
	{
		try
		{

			System.out.println("IP");
			Scanner in = new Scanner(System.in);
			InetAddress ip = InetAddress.getByName(in.nextLine());

			System.out.println("Download file");
			String filename = in.nextLine();

			byte[] down_file = filename.getBytes();
			byte[] buf = new byte[down_file.length + 1];
			buf[0] = REQ;
			for (int i = 0; i < down_file.length; i++)
			{
				buf[i + 1] = down_file[i];
			}
			DatagramPacket p = new DatagramPacket(buf, buf.length, ip, 44440);
			ds = new DatagramSocket();
			ds.send(p);

			OutputStream file_write = new FileOutputStream("test.jpg");

			boolean send = false;
			while (!send)
			{
				byte[] re = new byte[1472];
				DatagramPacket recieve = new DatagramPacket(re, 1472);
				ds.receive(recieve);
				byte[] data = recieve.getData();
				byte[] ack = new byte[9];
				if (data[0] == OK)
				{
					long size = extractLong(data, 1);
					System.out.println("the file size: " + size);

				} else if (data[0] == NOTOK)
				{
					System.out.println("Server can not found file!");
				} else if (data[0] == DATA)

				{
					ack[0] = ACK;
					DatagramPacket ackPacket = new DatagramPacket(ack,
							ack.length, recieve.getSocketAddress());

					if (recieve.getLength() == 1472)
					{

						file_write.write(data, 9, data.length - 9);
						storeLong(ack, 1, 1472);
						ds.send(ackPacket);
						Total_packet = Total_packet + recieve.getLength();

					}

					if (recieve.getLength() < 1472)
					{
						file_write.write(data, 9, recieve.getLength() - 9);
						storeLong(ack, 1, 1472);
						ds.send(ackPacket);
						send = true;
						file_write.close();
						Total_packet = Total_packet + recieve.getLength();

					}
					System.out.println("Receiving" + " from "
							+ recieve.getSocketAddress() + "--" + "PACKET: "
							+ recieve.getLength() + " TOTAL-NUM: "
							+ Total_packet);

				}
			}
		} catch (Exception e)
		{
			System.err.println("Exception: " + e);
		}

		System.out.println("Receiving---- finished!" + " Received"
				+ Total_packet);

		return;
	}

	public static void main(String args[])
	{
		StftpClient d = new StftpClient();
		d.startClient();
	}
}
