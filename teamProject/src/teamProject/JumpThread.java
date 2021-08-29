package teamProject;

public class JumpThread extends Thread {
	private int charX = 120, charY = 130;
	private int cX = 60, cY = 240;
	private int foot = 370;
	private boolean jumpnow = false;
	boolean fall = false;
	private boolean jump = false;
	private int field = 240;

	public JumpThread() {
		getFoot();
	}

	public void run() {
		while (true) {
			if (jump == false && fall == false && jumpnow == true) {
				jump = true;
				// 점프 사운드 위치
				while (true) {
					cY--;
					foot = cY + charY;
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (cY == 50) {
						fall = true;
						break;
					}

				}
			}
			if (jump == true && fall == true && foot < field) {
				fall = false;
				while (true) {
					cY++;
					foot = cY + charY;
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (cY == field) {
						jump = false;
						break;
					}

				}
				jumpnow = false;
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	int getcX() {
		return cX;
	}

	int getcY() {
		return cY;
	}

	int getcharX() {
		return charX;
	}

	int getcharY() {
		return charY;
	}

	void setcY() {
		cY = 240;
	}

	void setJumpnow(boolean t) {
		jumpnow = t;
	}

	boolean getJumpnow() {
		return jumpnow;
	}

	int getFoot() {
		return foot;
	}

}
