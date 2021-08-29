package teamProject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class BlockGame {
	static class MyFrame extends JFrame implements ActionListener{
		static Music introMusic;

		// constant
		static int BALL_WIDTH = 20;
		static int BALL_HEIGHT = 20;

		// 블럭 행 수
		static int BLOCK_ROWS = 5;

		// 블럭 열 수
		static int BLOCK_COLUMNS = 10;
		static int BLOCK_WIDTH = 40;
		static int BLOCK_HEIGHT = 20;
		// 블럭 사이의 간격
		static int BLOCK_GAP = 3;
		static int BAR_WIDTH = 100;
		static int BAR_HEIGHT = 10;
		static int CANVAS_WIDTH = 400 + (BLOCK_GAP * BLOCK_COLUMNS) - BLOCK_GAP;
		static int CANVAS_HEIGHT = 600;

		static int count = 0;

		// variable
		public MyPanel myPanel = null;
		static int score = 0;
		static Timer timer = null;
		static Block[][] blocks = new Block[BLOCK_ROWS][BLOCK_COLUMNS];
		static Bar bar = new Bar();
		static Ball ball = new Ball();
		static int barXTarget = bar.x; // Target Value - 보관(한번에 많은 값을 받아야해서)
		static int dir = 0; // <방향> 0: up - right 1: down - right 2: up - left 3: down -left
		static int ballSpeed = 6;
		public static boolean stop = false;
		public static boolean cstop = false;
		public boolean start = false;
		public boolean reStart = false;

		timeThread ttd = new timeThread();
		static ImageIcon icon1;
		static ImageIcon icon2;
		private JButton jb = new JButton("Start");
		JButton jb3 = new JButton("Start");

		public int musicState = 1;

		static int gScore; // DB로 보낼 점수
		static int gTime; // DB로 보낼 시간
		
		private DuduRecord dr;

		static class Ball {
			// 공의 위치
			int x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2; // 공을 캔버스의 정중앙에 위치(캔버스의 중앙 - 공의 가로/2)
			int y = (CANVAS_HEIGHT / 2) + 95;
			int width = BALL_WIDTH;
			int height = BALL_HEIGHT;

			Point getCenter() {// 공의 중앙점
				return new Point(x + (BALL_WIDTH / 2), y + (BALL_HEIGHT / 2));
			}

			Point getBottomCenter() {
				return new Point(x + (BALL_WIDTH), y + (BALL_HEIGHT));
			}

			Point getTopCenter() {
				return new Point(x + (BALL_WIDTH / 2), y);
			}

			Point getLeftCenter() {
				return new Point(x, y + (BALL_HEIGHT / 2));
			}

			Point getRightCenter() {
				return new Point(x + (BALL_WIDTH), y + (BALL_HEIGHT / 2));
			}
		}

		static class Bar {
			// 바의 위치
			int x = CANVAS_WIDTH / 2 - BAR_WIDTH / 2;
			int y = CANVAS_WIDTH;
			int width = BAR_WIDTH;
			int height = BAR_HEIGHT;
		}

		static class Block {
			// 블록은 각각 다르기때문에 초기값 0으로 설정
			int x = 0;
			int y = 0;
			int width = BLOCK_WIDTH;
			int height = BLOCK_HEIGHT;
			int color = 0; // 0: white 1: yellow 2: blue 3: mazanta 4: red
			boolean isHidden = false; // 충돌 후에 블록 사라지게 할 것
		}

		class MyPanel extends JPanel implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == jb) {
					timer = new Timer(20, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							movement();
							jb.setVisible(false);
							jb3.setVisible(true);
							jb3.setEnabled(false);
						}
					});

					timer.start();
					ttd.start();
				}

				else if (e.getSource() == jb3) {
					timer = new Timer(20, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							movement();
							jb3.setEnabled(false);
						}
					});
					reStart();
				}
			}

			public MyPanel() {
				JPanel jp1 = new JPanel(); // 게임화면

				this.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);

				jp1.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
				jp1.setBackground(Color.black);

				this.setLayout(null);
				jp1.setLayout(null);

				this.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
				jp1.setBounds(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);

				JButton jb1 = new JButton("Exit");
				JButton jb2 = new JButton("Record");

				jb.setBounds(70, 520, 70, 30);
				jb1.setBounds(170, 520, 70, 30);
				jb2.setBounds(270, 520, 80, 30);
				jb3.setBounds(70, 520, 70, 30);

				jb.addActionListener(this);
				jb2.addActionListener(this);
				jb3.addActionListener(this);

				jb.setFocusable(false); // 버튼이 다른 액션 방해하지 않도록
				jb1.setFocusable(false);
                jb2.setFocusable(false);
				jb3.setFocusable(false);

				jb1.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);

					}
				});

				jb2.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (dr == null) {
							dr = new DuduRecord(getScore(), 3);
						} else {
							dr.dispose();
							dr = new DuduRecord(getScore(), 3);
						}
					}
				});

				jp1.add(jb);
				jp1.add(jb1);
				jp1.add(jb2);
				jp1.add(jb3);

				jb3.setVisible(false);

				this.add(jp1);

				introMusic = new Music("mp3", true);
				introMusic.start();
			}

			public int getScore() {
				return gScore / gTime;
			}
			
			public void paint(Graphics g) {
				super.paint(g);
				Graphics2D g2d = (Graphics2D) g;
				icon1 = new ImageIcon("../teamProject/blockImagewin.png");
				icon2 = new ImageIcon("../teamProject/blockImage/blockImageGO.png");
				
				drawUI(g2d);
			}

			private void drawUI(Graphics2D g2d) {

				// draw Blocks
				for (int i = 0; i < BLOCK_ROWS; i++) {
					for (int j = 0; j < BLOCK_COLUMNS; j++) {
						if (blocks[i][j].isHidden) {
							continue;
						}
						if (blocks[i][j].color == 0) {
							g2d.setColor(Color.white);
						} else if (blocks[i][j].color == 1) {
							g2d.setColor(Color.yellow);
						} else if (blocks[i][j].color == 2) {
							g2d.setColor(Color.blue);
						} else if (blocks[i][j].color == 3) {
							g2d.setColor(Color.magenta);
						} else if (blocks[i][j].color == 4) {
							g2d.setColor(Color.red);
						}
						g2d.fillRect(blocks[i][j].x, blocks[i][j].y, blocks[i][j].width, blocks[i][j].height);// 사각형 그리기
					}

					// drow score
					g2d.setColor(Color.white);
					g2d.setFont(new Font("freeserifbold", Font.PLAIN, 18));
					g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 40, 23); // score 위치
					g2d.drawString("time : " + ttd.ss, CANVAS_WIDTH / 2 - 40, 45);// 스탑워치

					if (stop) {
						g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 40, 23); // score 위치
						g2d.drawImage(icon2.getImage(), CANVAS_WIDTH / 50, 150, null);
					}
					if (cstop) {
						g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 40, 23); // score 위치
						g2d.drawImage(icon1.getImage(), CANVAS_WIDTH / 50, 60, null);
					}
					if (reStart) {
						g2d.setColor(Color.white);
						g2d.setFont(new Font("freeserifbold", Font.PLAIN, 18));
						g2d.drawString("score : " + score, CANVAS_WIDTH / 2 - 40, 23); // score 위치
						g2d.drawString("time : " + ttd.ss, CANVAS_WIDTH / 2 - 40, 45);// 스탑워치
					}

					// draw Ball
					g2d.setColor(Color.white);
					g2d.fillOval(ball.x, ball.y, BALL_WIDTH, BALL_HEIGHT); // 원 그리기

					// draw bar
					g2d.setColor(Color.white);
					g2d.fillRect(bar.x, bar.y, bar.width, bar.height);
				}
			}
		}

		//SubFrame sf;
		public MyFrame() {
			super.setSize(CANVAS_WIDTH, CANVAS_HEIGHT);
			Dimension rscreen = Toolkit.getDefaultToolkit().getScreenSize();
			int rxpos = (int) (rscreen.getWidth() / 2 - getWidth() / 2);
			int rypos = (int) (rscreen.getHeight() / 2 - getHeight() / 2);
			setLocation(rxpos, rypos);
			super.setVisible(true);
			super.setLayout(new BorderLayout());
			super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기
			super.setResizable(false); // 창 사이즈 고정(최대화 금지)

			initData();
			myPanel = new MyPanel();
			super.add("Center", myPanel);

			setKeyListener();
			startTimer();
		}

		public void initData() { // 선언한 변수들 초기화
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block(); // 위에서 공간만 만들었기때문에 값을 넣어주기 위해 생성해야함
					blocks[i][j].x = BLOCK_WIDTH * j + BLOCK_GAP * j; // x축, 벽돌사이에 간격을 줘야함!
					blocks[i][j].y = 100 + BLOCK_HEIGHT * i + BLOCK_GAP * i; // y축my
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i; // 값 역전시켜야 색이 제대로 나옴(제일 밑이 하얀색이 나오게)
					blocks[i][j].isHidden = false; // 충돌이 일어나면 블록이 숨겨지도록!
				}
			}
		}

		public void setKeyListener() { // bar를 keybord로 움직이기
			this.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_LEFT) {// 화살표 왼쪽키를 누르면
						barXTarget -= 30;
						if (bar.x < barXTarget) {// 계속 키를 누를 경우
							barXTarget = bar.x;
						}
					} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {// 화살표 오른쪽키를 누르면
						barXTarget += 30;
						if (bar.x > barXTarget) {// 계속 키를 누를 경우
							barXTarget = bar.x;
						}
					}
				}
			});
		}

		public void startTimer() {
			timer = new Timer(20, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					// movement();
					checkCollision(); // 충돌 처리(벽과 bar)
					checkCollisionBlock(); // 블록들 충돌 처리

					myPanel.repaint(); // Redraw!

					// 게임 종료
					isGameFinish();
				}
			});
			timer.start();// 타이머 시작!
		}

		public void isGameFinish() {

			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if (block.isHidden)
						count++;
				}
			}
			if (score == 1500) {
				cStop();
			}
		}

		public void stop() {
			// 게임 종료
			timer.stop();
			ttd.stop();
			stop = true;
			jb3.setEnabled(true);
			introMusic.close();
			myPanel.repaint();
			ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2;
			ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
			timer.removeActionListener(myPanel);

			gScore = score;
			gTime = ttd.ss;
		}

		public void cStop() {
			// 게임 종료
			timer.stop();
			ttd.stop();
			cstop = true;
			jb3.setEnabled(true);
			introMusic.close();
			myPanel.repaint();
			ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2;
			ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
			timer.removeActionListener(myPanel);

			gScore = score;
			gTime = ttd.ss;
		}

		public void reStart() {

			int mr = (int) (Math.random() * 3) + 0;
			if (mr == 0 || mr == 2) {
				dir = mr; // 공 방향 리셋
			}
			score = 0;
			ttd.ss = 0;

			// 다시그리기
			ball.x = CANVAS_WIDTH / 2 - BALL_WIDTH / 2;
			ball.y = CANVAS_HEIGHT / 2 - BALL_HEIGHT / 2;
			bar.x = CANVAS_WIDTH / 2 - BAR_WIDTH / 2;
			bar.y = CANVAS_WIDTH / 2 + 210;

			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					blocks[i][j] = new Block();
					blocks[i][j].x = BLOCK_WIDTH * j + BLOCK_GAP * j;
					blocks[i][j].y = 100 + BLOCK_HEIGHT * i + BLOCK_GAP * i;
					blocks[i][j].width = BLOCK_WIDTH;
					blocks[i][j].height = BLOCK_HEIGHT;
					blocks[i][j].color = 4 - i;
					blocks[i][j].isHidden = false;
				}
			}
			introMusic = new Music("mp3", true);
			introMusic.start();
			stop = false;
			reStart = true;
			timer.start();
			ttd = new timeThread();
			ttd.start();

		}

		public static void movement() {
			if (bar.x < barXTarget) {
				bar.x += 5;
			} else if (bar.x > barXTarget) {
				bar.x -= 5;
			}
			// 공의 방향
			if (dir == 0) {// 0: up - right
				ball.x += ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 1) {// down - right
				ball.x += ballSpeed;
				ball.y += ballSpeed;
			} else if (dir == 2) {// up - left
				ball.x -= ballSpeed;
				ball.y -= ballSpeed;
			} else if (dir == 3) {// down - left
				ball.x -= ballSpeed;
				ball.y += ballSpeed;
			}
		}

		public boolean duplRect(Rectangle rect1, Rectangle rect2) {
			return rect1.intersects(rect2); // 두 개의 사각형 영역이 중복되는지 확인
		}

		public void checkCollision() {// 충돌 처리(벽과 bar)
			if (dir == 0) {// 0: up - right
				// 벽
				if (ball.y < 0) {// 위쪽 벽에 부딪쳤을때
					dir = 1;
				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) {// 오른쪽 벽에 부딪쳤을때
					dir = 2;
				}
				// bar - none(위로 가기 때문에 bar와 부딪힐 일이 없음)
			} else if (dir == 1) {// down - right
				// 벽
				if (ball.y > (CANVAS_HEIGHT - 10) - BALL_HEIGHT - BALL_HEIGHT) {// 아래쪽 벽에 부딪쳤을때

					stop();

				}
				if (ball.x > CANVAS_WIDTH - BALL_WIDTH) {// 오른쪽 벽에 부딪쳤을때
					dir = 3;
				}
				// bar
				if (ball.getBottomCenter().y >= bar.y) {
					if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 0;
					}
				}
			} else if (dir == 2) {// up - left
				// 벽
				if (ball.y < 0) {// 위쪽 벽에 부딪쳤을때
					dir = 3;
				}
				if (ball.x < 0) {// 왼쪽 벽에 부딪쳤을때
					dir = 0;
				}
				// bar - none(위로 가기 때문에 bar와 부딪힐 일이 없음)
			} else if (dir == 3) {// down - left
				// 벽
				if (ball.y > (CANVAS_HEIGHT - 10) - BALL_HEIGHT - BALL_HEIGHT) {// 아래쪽 벽에 부딪쳤을때
					stop();
				}
				if (ball.x < 0) {// 왼쪽 벽에 부딪쳤을때
					dir = 1;
				}
				// bar
				if (ball.getBottomCenter().y >= bar.y) {
					if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
							new Rectangle(bar.x, bar.y, bar.width, bar.height))) {
						dir = 2;
					}
				}
			}
		}

		public void checkCollisionBlock() { // 블록들에 대한 충돌 처리
			// <방향> 0: up - right 1: down - right 2: up - left 3: down -left
			for (int i = 0; i < BLOCK_ROWS; i++) {
				for (int j = 0; j < BLOCK_COLUMNS; j++) {
					Block block = blocks[i][j];
					if (block.isHidden == false) {
						if (dir == 0) {
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// 공은 원형이라 모서리에 부딪히면 애매할 수 있어서 더 안쪽으로 들어갈 수 있게 +-2
									// 블록의 아래쪽에 부딪쳤을때
									dir = 1;

								} else {
									// 블록의 왼쪽 벽에 부딪쳤을때
									dir = 2;

								}
								block.isHidden = true;
								if (block.color == 0) { // 흰색 블록 깨면 10점 추가
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
							
						} else if (dir == 1) {
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// 공은 원형이라 모서리에 부딪히면 애매할 수 있어서 더 안쪽으로 들어갈 수 있게 +-2
									// 블록의 위쪽에 부딪쳤을때
									dir = 0;
								} else {
									// 블록의 왼쪽 벽에 부딪쳤을때
									dir = 3;
								}
								block.isHidden = true;
								if (block.color == 0) { // 흰색 블록 깨면 10점 추가
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}

						} else if (dir == 2) {
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// 공은 원형이라 모서리에 부딪히면 애매할 수 있어서 더 안쪽으로 들어갈 수 있게 +-2
									// 블록의 아래쪽에 부딪쳤을때
									dir = 3;
								} else {
									// 블록의 오른쪽 벽에 부딪쳤을때
									dir = 0;
								}
								block.isHidden = true;
								if (block.color == 0) { // 흰색 블록 깨면 10점 추가
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
						} else if (dir == 3) {
							if (duplRect(new Rectangle(ball.x, ball.y, ball.width, ball.height),
									new Rectangle(block.x, block.y, block.width, block.height))) {
								if (ball.x > block.x + 2 && ball.getRightCenter().x <= block.x + block.width - 2) {
									// 공은 원형이라 모서리에 부딪히면 애매할 수 있어서 더 안쪽으로 들어갈 수 있게 +-2
									// 블록의 위쪽에 부딪쳤을때
									dir = 2;

								} else {
									// 블록의 오른쪽 벽에 부딪쳤을때
									dir = 1;
								}
								block.isHidden = true;
								if (block.color == 0) { // 흰색 블록 깨면 10점 추가
									score += 10;
								} else if (block.color == 1) {
									score += 20;
								} else if (block.color == 2) {
									score += 30;
								} else if (block.color == 3) {
									score += 40;
								} else if (block.color == 4) {
									score += 50;
								}
							}
						}
					}
				}
			}
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
}