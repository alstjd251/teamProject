package teamProject;

public class timeThread extends Thread {

	int ss = 0;

	timeThread() {

	}

	public void run() {
		try {
			for (int i = 0; i < 1000000000; i++) {
				Thread.sleep(1000);
				ss++;
				if (ss == 60) {
					break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
