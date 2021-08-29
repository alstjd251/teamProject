package teamProject;

public class Tacle extends Thread {
	private int tX = 700;
	private int tsizex, tsizey, tY;
	private int random, speed, score = 0, setfoot;
	int heart = 3;

	public int getRandom() {
		return random;
	}

	public int getTx() {
		return tX;
	}

	public int getTy() {
		return tY;
	}

	public void setTx(int tX) {
		this.tX = tX;
	}

	public int getTsizex() {
		return tsizex;
	}

	public int getTsizey() {
		return tsizey;
	}

	public void setFoot(int a) {
		this.setfoot = a;
	}

	public int getScore() {
		return score;
	}

	public int getHeart() {
		return heart;
	}

	public void setHeart(int a) {
		this.heart = a;
	}
	public void run() {
		while (true) {
			random = (int) (Math.random() * 2);
			speed = (int) (Math.random() * 3) + 2;
			switch (random) {
			case 0:
				tsizex = 50;
				tsizey = 90;
				tY = 270;
				break;
			case 1:
				tsizex = 130;
				tsizey = 70;
				tY = 290;
				break;
			}
			while (true) {
				tX--;
				int tX2 = tX + tsizex - 30;
				if (tX <= 100 && tX2 >= 100 && setfoot >= tY) {
					tX = 700;
					score -= 25;
					heart--;
					break;
				} else if (tX <= -130) {
					tX = 700;
					if (speed == 4 & random == 1) {
						score += 60;
					} else {
						score += 30;
					}
					break;
				}
				try {
					Thread.sleep(speed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
