import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Chat implements Runnable {

	@Override
	public void run() {
		BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				Network.messages.add(bf.readLine());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
