import java.net.DatagramPacket;  
import java.net.DatagramSocket;  
import java.net.InetAddress;  
  
public class Client {  
  
    /** 
     * @param args 
     * @throws Exception  
     */  
    public static void main(String[] args) throws Exception {  
          
        //DatagramSocket�����ʾ�������ͺͽ������ݱ������׽��֡�   
        DatagramSocket ds = new DatagramSocket();  
  
        String str = "Hello World";  
        //DatagramPacket�����ʾ���ݱ�����   
        DatagramPacket dp = new DatagramPacket(str.getBytes(), str.length(),   
                InetAddress.getByName("localhost"), 7000);  
          
        //�������ݱ�  
        ds.send(dp);  
          
        byte[] by = new byte[1000];  
        // ���� DatagramPacket���������ճ���Ϊ length �����ݰ���  
        DatagramPacket dp2 = new DatagramPacket(by,100);  
        ds.receive(dp2);  
          
        System.out.println(new String(by,0,dp2.getLength()));  
          
        ds.close();  
          
    }  
  
}  