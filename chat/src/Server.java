import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
  
public class Server {  
    public static void main(String[] args) throws Exception {  
          
        //DatagramSocket�����ʾ�������ͺͽ������ݱ������׽��֡�   
        DatagramSocket ds = new DatagramSocket(7000);  
  
        byte[] by = new byte[1000];  
        // ���� DatagramPacket���������ճ���Ϊ length �����ݰ���  
        DatagramPacket dp = new DatagramPacket(by,1000);  
        ds.receive(dp);  
          
        System.out.println(new String(by,0,dp.getLength()));  
          
        String str = "welcome";  
        //DatagramPacket�����ʾ���ݱ����� dp.getAddress()�õ����յ���DatagramPacket�������ĵ�ַ  
        DatagramPacket dp2 = new DatagramPacket(str.getBytes(), str.length(),   
                dp.getAddress(), dp.getPort());  
          
        //�������ݱ�  
        ds.send(dp2);  
          
        ds.close();  
    }  
   
} 