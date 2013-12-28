import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;

public class TcpSender implements Runnable {

	String mac;

	public TcpSender(String mac) {
		this.mac = mac;
	}

	@Override
	public void run() {

		String sentence;
		String modifiedSentence;

		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(
				System.in));
		Socket clientSocket;
		String ip = Network.fromMactoIp.get(mac);
		try {
						for (int i = Network.countOfMessages.get(mac); i < Network.messages
					.size(); i++) {
							
							clientSocket = new Socket(ip, 1236);
							DataOutputStream outToServer = new DataOutputStream(
									clientSocket.getOutputStream());
							BufferedReader inFromServer = new BufferedReader(
									new InputStreamReader(clientSocket.getInputStream()));
							System.err.println(Network.messages.size());

							long time = System.currentTimeMillis()
						- Network.difTime.get(mac);
				byte[] senByte = Network.messages.get(i).getBytes();
				outToServer.writeLong(time);
				//outToServer.write(mac);
				outToServer.writeInt(senByte.length);
				outToServer.write(senByte);
				Network.countOfMessages.put(mac, (i + 1));
				
				clientSocket.close();
			}
			// modifiedSentence = inFromServer.readLine();
			// System.out.println("FROM SERVER: " + modifiedSentence);
			
		} catch (UnknownHostException e) {

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
