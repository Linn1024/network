import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

public class UDPReceiver implements Runnable {
	public UDPReceiver(DatagramChannel channel, int port) {
		this.port = port;
		this.channel = channel;
		// this.msgCnt = new HashMap<String, Integer>();
	}

	@Override
	public void run() {
		try {
			channel = DatagramChannel.open();
			channel.socket().setBroadcast(true);
			channel.socket().bind(new InetSocketAddress(1235)); // Binds this
																// DatagramSocket
																// to a specific
																// address &
																// port.
			// For UDP sockets it may b0e necessary to bind more than one socket
			// to the same socket address.
			// This is typically for the purpose of receiving multicast packets
			channel.socket().setReuseAddress(true);

			while (true) {
				ByteBuffer bf = ByteBuffer.allocate(16);
				SocketAddress sa = channel.receive(bf);
				//channel.receive(bf);
				// System.out.println(channel.getLocalAddress().toString());
				bf.flip();
				long time = bf.getLong();
				byte[] mac = new byte[6];

				bf.get(mac);
				// System.err.print(fromByteToString(mac));

				Network.fromMactoIp.put(fromByteToString(mac), sa.toString().replaceAll("/|:.*", ""));
				Network.difTime.put(fromByteToString(mac),
						System.currentTimeMillis() - time);
				if (Network.countOfMessages.get(fromByteToString(mac)) == null) {
					Network.countOfMessages.put(fromByteToString(mac), 0);
				}
				// network.chat.add(channel.getLocalAddress().toString());t
				// TcpSender tcpSender = new TcpSender(fromByteToString(mac));
				// Thread tst = new Thread(tcpSender);
				// tst.start();
				send(mac);
				/*
				 * byte[] ip = new byte[4]; String IP; IP = new
				 * Byte(ip[0]).toString()+"."+new Byte(ip[1]).toString()+"."+new
				 * Byte(ip[2]).toString()+"."+new Byte(ip[3]).toString();
				 * bf.get(ip); byte[] nameB = new byte[20]; bf.get(nameB);
				 * //String name = fromBytesToString(nameB); String name = new
				 * String(nameB, "UTF-8"); long time = bf.getLong(); byte[]
				 * compnameB = new byte[20]; bf.get(compnameB); String compname
				 * = new String(compnameB, "UTF-8"); if
				 * (network.res.containsKey(IP)== false) { network.res.put(IP,
				 * new network.letter()); } network.res.get(IP).time = time;
				 * network.res.get(IP).name = name; network.res.get(IP).compname
				 * = compname; network.res.get(IP).ip = ip;
				 */
				// System.out.println((ip[0]+256) % 256+ "."+ (ip[1]+256) % 256+
				// "."+ (ip[2]+256) % 256+"."+(ip[3]+256) % 256+ "    " + name +
				// "    "+ time + "   "+ compname);
			}

		} catch (IOException e) {
			e.printStackTrace(); // To change body of catch statement use File |
									// Settings | File Templates.
		}
	}

	public void send(byte[] mac1) {

		String sentence;
		String modifiedSentence;
		String mac = fromByteToString(mac1);
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
				//System.err.println(Network.messages.size());

				long time = System.currentTimeMillis()
						- Network.difTime.get(mac);
				byte[] senByte = Network.messages.get(i).getBytes();
				outToServer.writeLong(time);
				outToServer.write(mac1);
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

	public String fromByteToString(byte[] p) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < p.length; i++) {
			sb.append(String.format("%02X%s", p[i], (i < p.length - 1) ? "-"
					: ""));
		}
		return sb.toString();
	}

	/*
	 * public String fromBytesToString(byte[] p){ StringBuilder sb = new
	 * StringBuilder(); for (int i = 0; i < p.length; i++) {
	 * sb.append(String.format("%02X%s", p[i], (i < p.length - 1) ? "-" : ""));
	 * } return sb.toString(); }
	 */

	public DatagramChannel channel;
	public static int port;
	public Map<String, Integer> msgCnt;

}
