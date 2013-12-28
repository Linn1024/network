import java.util.Calendar;

public class Print implements Runnable {

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (String b : Network.res.keySet()) {
				if (Calendar.getInstance().getTimeInMillis()/1000 - Network.res.get(b).time > 15){
					continue;
				}
				System.out.println((Network.res.get(b).ip[0] + 256) % 256 + "." + (Network.res.get(b).ip[1] + 256) % 256
						+ "." + (Network.res.get(b).ip[2] + 256) % 256 + "." + (Network.res.get(b).ip[3] + 256) % 256
						+ "     " + Network.res.get(b).name + "      "
						+ Network.res.get(b).time + "      "
						+ Network.res.get(b).compname);
			}
		}
	}
}
