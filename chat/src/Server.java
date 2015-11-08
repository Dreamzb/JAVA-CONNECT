import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
  
public class Server {  
    public static void main(String[] args) throws Exception {  
          
        //DatagramSocket此类表示用来发送和接收数据报包的套接字。   
        DatagramSocket ds = new DatagramSocket(7000);  
  
        byte[] by = new byte[1000];  
        // 构造 DatagramPacket，用来接收长度为 length 的数据包。  
        DatagramPacket dp = new DatagramPacket(by,1000);  
        ds.receive(dp);  
          
        System.out.println(new String(by,0,dp.getLength()));  
          
        String str = "welcome";  
        //DatagramPacket此类表示数据报包。 dp.getAddress()得到接收到的DatagramPacket的主机的地址  
        DatagramPacket dp2 = new DatagramPacket(str.getBytes(), str.length(),   
                dp.getAddress(), dp.getPort());  
          
        //发送数据报  
        ds.send(dp2);  
          
        ds.close();  
    }  
   
} 