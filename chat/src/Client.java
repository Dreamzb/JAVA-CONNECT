import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;  
  
public class Client {  
  
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
          
        //DatagramSocket此类表示用来发送和接收数据报包的套接字。   
        DatagramSocket ds = new DatagramSocket();  
  
        String str = "Hello World";  
        //DatagramPacket此类表示数据报包。   
        DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(),   
                InetAddress.getByName("localhost"), 7000);  
          
        //发送数据报  
        ds.send(dp);  
          
        byte[] by = new byte[1000];  
        // 构造 DatagramPacket，用来接收长度为 length 的数据包。  
        DatagramPacket dp2 = new DatagramPacket(by,100);  
        ds.receive(dp2);  
          
        System.out.println(new String(by,0,dp2.getLength()));  
          
        ds.close();  
          
    }  
  
}  