import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TcpReceiver implements Runnable {

	@Override
	public void run() {
		byte[] clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket;
		while (true) {
			try {
				Thread.sleep(1000);
				welcomeSocket = new ServerSocket(1236);
				Socket connectionSocket = welcomeSocket.accept();
				DataInputStream inFromClient = new DataInputStream(
						connectionSocket.getInputStream());
				// DataOutputStream outToClient = new DataOutputStream(
				// connectionSocket.getOutputStream());
				long time;
				try {
					time = inFromClient.readLong();
				} catch (IOException er) {
					connectionSocket.close();
					welcomeSocket.close();
					continue;

				}
				byte[] mac = new byte[6];
				inFromClient.read(mac);
				int n;
				n = inFromClient.readInt();
//				/System.err.println(n);
				clientSentence = new byte[n];
				inFromClient.readFully(clientSentence);
				String toPrint = new String(clientSentence, "UTF-8");
				System.out.println(fromByteToString(mac) + "    " + new Date(time) + " " + toPrint);
				//System.err.println("good");
				connectionSocket.close();
				welcomeSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//System.err.print("bad");
				e.printStackTrace();
			}
			// capitalizedSentence = clientSentence.toUpperCase() + '\n';
			// outToClient.writeBytes(capitalizedSentence);
 catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	public String fromByteToString(byte[] p) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < p.length; i++) {
			sb.append(String.format("%02X%s", p[i], (i < p.length - 1) ? "-"
					: ""));
		}
		return sb.toString();
	}
}