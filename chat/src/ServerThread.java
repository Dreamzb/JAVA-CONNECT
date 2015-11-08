import java.net.*;
import java.io.*;
import java.util.*;

class HttpServerSession extends Thread
{
	private HttpServer server;
	private Socket client;
	private String request;

	public void run()
	{
		System.out.println("Your IP address is "+ client.getInetAddress().getHostAddress());
		try
		{
			BufferedOutputStream writer = new BufferedOutputStream(client.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			request = reader.readLine();
			System.out.println(request);
			String parts[] = request.split(" ");
			if (parts[0].compareTo("GET") == 0)
			{
				String filename = parts[1];
				if (filename.endsWith("/"))
				{
					filename = "index.html";
				}
				else
				{
					filename = filename.substring(1);
				}
				while (true)
				{
					String line = reader.readLine();
					if (line == null)
					{
						System.out.println("error");
					}
					if (line.compareTo("") == 0)
					{
						break;
					}
				}
				System.out.println(filename);
				FileInputStream file200 = new FileInputStream(filename);
				println(writer, "HTTP/1.1 200 OK");
				println(writer, "");
				println(writer, "200");
				println(writer, "");
				returnfile(file200, writer);
				writer.flush();
				writer.close();
			}
		}
		catch (FileNotFoundException e)
		{
			try
			{
				BufferedOutputStream writer = new BufferedOutputStream(
						client.getOutputStream());
				println(writer, "HTTP/1.1 404 OK");
				println(writer, "");
				println(writer, "404");
				println(writer, "");
				FileInputStream file404 = new FileInputStream("404.html");
				returnfile(file404, writer);
				writer.flush();
				writer.close();
				file404.close();
			}
			catch (Exception ex)
			{
				System.err.println("Exception: " + ex);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		server.logout(this);
	}

	public HttpServerSession(HttpServer a, Socket b)
	{
		server = a;
		client = b;
	}

	private void println(BufferedOutputStream bos, String s) throws IOException
	{
		String news = s + "\r\n";
		byte[] array = news.getBytes();
		for (int i = 0; i < array.length; i++)
		{
			bos.write(array[i]);
		}
		return;
	}

	private void returnfile(FileInputStream f, BufferedOutputStream bos)
	{
		try
		{
			while (true)
			{
				byte[] file = new byte[1024];
				int rf;
				if ((rf = f.read(file)) == -1)
				{
					break;
				}
				else
				{
					bos.write(file, 0, rf);
					sleep(100);
					bos.flush();
				}
			}
			f.close();
			bos.close();
		}
		catch (Exception e)
		{
			System.err.println("Exception: " + e);
		}
	}
}

class HttpServer
{
	private ArrayList<HttpServerSession> sessions;

	public void logout(HttpServerSession session)
	{
		int i, len;
		synchronized (sessions)
		{
			len = sessions.size();
			for (i = 0; i < len; i++)
			{
				HttpServerSession s = sessions.get(i);
				if (s == session)
				{
					sessions.remove(i);
					break;
				}
			}
		}
	}

	public void start_server()
	{
		sessions = new ArrayList<HttpServerSession>();
		try
		{
			/*ListIterator upto = peers.listIterator(0); 
			 while ( upto.hasNext() ){ 
			    Peer peer = upto.next(); 
			    ...
			 }*/
			ServerSocket ss = new ServerSocket(40080);
			System.out.println("web server starting");
			System.out.println("prot number is 40080");
			while (true)
			{
				Socket client = ss.accept();
				HttpServerSession thread = new HttpServerSession(this, client);
				synchronized (sessions)
				{
					sessions.add(thread);
				}
				thread.start();
				System.out.println("a connection was received");
			}
		}
		catch (Exception e)
		{
			System.err.println("Exception: " + e);
		}
	}

	public static void main(String args[])
	{
		HttpServer server = new HttpServer();
		server.start_server();
	}
	
}
