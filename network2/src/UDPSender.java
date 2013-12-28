import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Calendar;
import java.util.Enumeration;

public class UDPSender implements Runnable {

	public byte[] ipToByteArray(String ipS) {
		byte[] ipB = new byte[4];
		String[] ipSS = ipS.split("\\.");
		for (int i = 0; i < 4; i++) {
			ipB[i] = (byte) (Integer.parseInt(ipSS[i]));
		}
		return ipB;
	}

	@Override
	public void run() {

		// System.err.print("lkhk");

		/*
		 * DatagramSocket sock = null; try { sock = new DatagramSocket(1234);
		 * sock.setBroadcast(true);
		 * sock.connect(InetAddress.getByName("255.255.255.255"), 1234);
		 * 
		 * } catch (SocketException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); } catch (UnknownHostException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */

		while (true) {
			try {
				Thread.sleep(2000);
				String compName = System.getProperty("user.name");
				InetAddress localAddr = InetAddress.getLocalHost();

				// System.err.print(s);

				// SocketAddress sa = new InetSocketAddress("10.0.0.112", 1234);

				SocketAddress sa = new InetSocketAddress(
						Inet4Address.getByName("255.255.255.255"), 1235);

				String ip = localAddr.getHostAddress();
				String name = localAddr.getHostName();
				while (name.length() < 20) {
					name += "\0";
				}
				while (compName.length() < 20) {
					compName += "\0";
				}
				long time = System.currentTimeMillis();

				// create message
				// ByteBuffer buf = ByteBuffer.allocate(14); // 8 time + 6 mac
				ByteBuffer buf = ByteBuffer.allocate(8 + 6);
				// SocketAddress sa = channel.receive(buf);
				buf.clear();
				// buf.put(ipToByteArray(ip));
				// buf.put(name.getBytes());
				buf.putLong(time);
				// buf.put(compName.getBytes());

				// NetworkInterface networ ks = null;
				InetAddress ipp = InetAddress.getLocalHost();
				//System.out.println("Current IP address : "
					//	+ ipp.getHostAddress());

				byte[] mac = null;
				Enumeration<NetworkInterface> networks = NetworkInterface
						.getNetworkInterfaces();
				while (networks.hasMoreElements()) {
					NetworkInterface network = networks.nextElement();
					mac = network.getHardwareAddress();

					if (mac != null) {
						//System.out.print("Current MAC address : ");

						StringBuilder sb = new StringBuilder();
						
						for (int i = 0; i < mac.length; i++) {
							sb.append(String.format("%02X%s", mac[i],
									(i < mac.length - 1) ? "-" : ""));
						}
						//System.out.println(sb.toString());
						if (sb.toString().length() > 0)
							break;
					}
				}
				
				buf.put(mac);
				buf.flip();

				// System.err.println("==========");
				// .System.err.println("Sending message...");
				// channel.setOption(, value)

				// DatagramPacket dp = new DatagramPacket(mac, mac.length);
				// sock.receive(dp);
				/*
				 * 
				 * DatagramPacket packet= new DatagramPacket(mac, mac.length,
				 * InetAddress.getByName("255.255.255.255"), 1234);
				 * sock.send(packet);
				 */
				int m = channel.send(buf, sa);
				// System.err.println(m);

			} catch (Exception e) {
				e.printStackTrace(); // To change body of catch statement use
										// File | Settings | File Templates.
			}
		}
	}

	public UDPSender(DatagramChannel channel) {

		this.channel = channel;
	}

	public static DatagramChannel channel;
}
