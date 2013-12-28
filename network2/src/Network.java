import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;



public class Network {

	public static class letter{
		public void letter(){
			this.count = 0;
			this.time = 0;
			this.compname = "";
			this.name = "";
			
			
		}
		public int count;
		public byte[] ip;
		public long time;
		public String compname;
		public String name;
		
	}
	
	static public volatile ArrayList<String> messages = new ArrayList<>();
	
	static public volatile HashMap<String, Integer> countOfMessages = new HashMap<>();
	
	static public volatile HashSet<String> chat = new HashSet<>();
	static public volatile HashMap<String, String> fromMactoIp = new HashMap<>();
	static public volatile HashMap<String, Long> difTime = new HashMap<>();
	
	static public volatile Map<String, letter> res = new HashMap<String, Network.letter>();
	
	public static void main(String[] args) throws IOException {
		DatagramChannel channel = DatagramChannel.open();
		 
		//System.err.print("lkhk");
		channel.connect(new InetSocketAddress("255.255.255.255",1235));
		channel.socket().setBroadcast(true);
		//channel.socket().bind(new InetSocketAddress(1234));
		   
		channel.socket().setReuseAddress(true);
		channel.configureBlocking(true);
      
		UDPSender USender = new UDPSender(channel);
        Thread tus = new Thread(USender);
        tus.start();
		UDPReceiver UReceiver = new UDPReceiver(channel, 1235);
		Thread tur = new Thread(UReceiver);
        tur.start();
        TcpReceiver TReceiver = new TcpReceiver();
		Thread ttr = new Thread(TReceiver);
        ttr.start();
        Chat chat = new Chat();
        Thread tchat = new Thread(chat);
        tchat.start();
        
         
        //Print pr = new Print();
        //Thread tp = new Thread(pr);
        //tp.start();
		
	}



}
