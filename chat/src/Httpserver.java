import java.io.*;
import java.util.Random;
import java.net.*;

public class Httpserver {
	
	
	public static void main(String[] args)
	{    
		try{
			int i = 0;
			i = (int)(Math.random()*32769) + 40000;
			ServerSocket servers = new ServerSocket(i);
			System.out.println("System online. The Portal is " + i);
			 
			while(true) {
				Socket client= servers .accept();
				BufferedInputStream in = new BufferedInputStream(client.getInputStream());
				BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
				ServerThread thread = new ServerThread(client);
				byte[] byt = new byte[10240];
			    in.read(byt);
			    out.write(byt);
				thread.start();
			}
		}
		catch(Exception e){
		    System.err.println("Exception: " + e);
		}
	}
	
}