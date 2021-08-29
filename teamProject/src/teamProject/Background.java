package teamProject;

public class Background extends Thread {
	private int backX1 = 0;
	private int backX2 = 2405;
	private int speed = 2;

	int getbackX1() {
		return backX1;
	}

	int getbackX2() {
		return backX2;
	}

	void setbackX1() {
		backX1 = 0;
	}

	void setbackX2() {
		backX2 = 2405;
	}

	public void run() {
		while (true) {
			backX1--;
			backX2--;
			if (backX2 == 0) {
				backX1 = 2405;
			}
			if (backX1 == 0) {
				backX2 = 2405;
			}
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

}
