package teamProject;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JGame extends JFrame implements ActionListener{
	private JPanel buttonSet;
	private JPanel playGround;
	private JButton startbutton = new JButton("Start");
	private JButton record = new JButton("Record");
	private Background bgt = new Background();
	private JumpThread jT = new JumpThread();
	private Timer time;
	private Tacle tc = new Tacle();
	private MusicThread intro;
	private int startInfo = 0;
	private Image bg = new ImageIcon("../teamProject/src/teamProject/jumpres/bg/bgLongg.jpg").getImage();
	private Image chargif = new ImageIcon("../teamProject/src/teamProject/jumpres/image/character.gif").getImage();
	private Image tacle1 = new ImageIcon("../teamProject/src/teamProject/jumpres/image/tacle1.png").getImage();
	private Image tacle2 = new ImageIcon("../teamProject/src/teamProject/jumpres/image/tacle2.png").getImage();
	private Image[] tacle = new Image[2];
	private JLabel timer = new JLabel("Ready");
	private int score1 = 0;
	private JLabel scorejl = new JLabel("Score : " + score1);
	private Setter st;

	private DuduRecord dr;
	
	private int heart1 = 3;
	private JLabel heart = new JLabel("목숨 : " + heart1);
	
	public JGame() {
		setAlwaysOnTop(true);
	    setVisible(true);
		setSize(600, 500);
		Dimension rscreen = Toolkit.getDefaultToolkit().getScreenSize();
		int rxpos = (int) (rscreen.getWidth() / 2 - getWidth() / 2);
		int rypos = (int) (rscreen.getHeight() / 2 - getHeight() / 2);
		setLocation(rxpos, rypos);
		setTitle("JumpingMan");
		setVisible(true);
		setResizable(false);
		setLayout(new BorderLayout());
		Container c = getContentPane();

		buttonSet = new JPanel();
		playGround = new ImagePanel();
		playGround.setLayout(null);

		// 타이머 관련
		timer.setForeground(Color.WHITE);
		timer.setFont(new Font("고딕", 30, 30));
		timer.setHorizontalAlignment(JLabel.CENTER);
		timer.setBounds(0, 0, 600, 80);
		playGround.add(timer);

		// 점수
		scorejl.setForeground(Color.WHITE);
		scorejl.setFont(new Font("고딕", 30, 30));
		scorejl.setHorizontalAlignment(JLabel.RIGHT);
		scorejl.setBounds(0, 0, 550, 80);
		playGround.add(scorejl);

		// 버튼들 관련
		buttonSet.add(startbutton);
		buttonSet.add(record);
		add(buttonSet, BorderLayout.SOUTH);
		add(playGround, BorderLayout.CENTER);
		
		// 체력
		heart.setForeground(Color.WHITE);
		heart.setFont(new Font("고딕", 30, 30));
		heart.setHorizontalAlignment(JLabel.LEFT);
		heart.setBounds(50, 0, 550, 80);
		playGround.add(heart);

		for (int i = 0; i < 2; i++) {
			tacle[i] = new ImageIcon("../teamProject/src/teamProject/jumpres/image/tacle" + (i + 1) + ".png").getImage();
		}

		// 시작버튼/리셋버튼
		  startbutton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                if (startbutton.getText().equals("Start")) {
	                    if (startInfo == 0) {
	                        bgt = new Background();
	                        bgt.start();
	                        intro = new MusicThread("mp3", true);
	                        intro.start();
	                        jT = new JumpThread();
	                        jT.start();
	                        tc = new Tacle();
	                        tc.start();
	                        time = new Timer();
	                        time.start();
	                        st = new Setter();
	                        st.start();
	                        startbutton.setText("Reset");
	                        startInfo = 1;
	                    }
	                } else if (startbutton.getText().equals("Reset")) {
	                    if (startInfo == 1) {
	                        bgt.stop();
	                        bgt.setbackX1();
	                        bgt.setbackX2();
	                        intro.close();
	                        jT.stop();
	                        jT.setcY();
	                        tc.stop();
	                        tc.setTx(700);
	                        time.stop();
	                        st.stop();
	                        timer.setText("Ready");
	                        tc.setHeart(5);
	                        startbutton.setText("Start");
	                        startInfo = 0;
	                    }
	                }

	            }
	        });
		  record.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	if (dr == null) {
						dr = new DuduRecord(getScore(), 2);
					} else {
						dr.dispose();
						dr = new DuduRecord(getScore(), 2);
					}
	            }
		  });
	}
	
	public int getScore() {
		return score1;
	}

	// 이미지 패널
	public class ImagePanel extends JPanel {
		private Image buffImage;
		private Graphics buffg;

		public ImagePanel() {
			addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						jT.setJumpnow(true);
					}
				}
			});
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			// 더블 버퍼링을 위한 코드
			if (buffg == null) {
				buffImage = createImage(this.getWidth(), this.getHeight()); // 버퍼링용 이미지 생성
				if (buffImage == null) {
					System.out.println("더블 버퍼링용 오프 스크린 생성 실패");
				} else
					buffg = buffImage.getGraphics();
			}
			// 배경 버퍼
			buffg.drawImage(bg, bgt.getbackX1(), 0, null);
			buffg.drawImage(bg, bgt.getbackX2(), 0, null);
			// 캐릭터 버퍼
			buffg.drawImage(chargif, jT.getcX(), jT.getcY(), jT.getcharX(), jT.getcharY(), null);
			// 장애물
			buffg.drawImage(tacle[tc.getRandom()], tc.getTx(), tc.getTy(), tc.getTsizex(), tc.getTsizey(), null);

			g.drawImage(buffImage, 0, 0, this);
			repaint();
		}

	}

	// 타이머
    public class Timer extends Thread {
        public void run() {
            int time = 60;
            timer.setText("Start!!");
            while (true) {
                playGround.requestFocus();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time--;
                timer.setText(time + "");
                if (time == 0) {
                    timer.setText("GameOver");
                    bgt.stop();
                    intro.close();
                    jT.stop();
                    tc.stop();
                    st.stop();
                    break;
                }
                else if (heart1 == 0) {
					timer.setText("GameOver");
					bgt.stop();
					jT.stop();
					tc.stop();
					st.stop();
					intro.close();
					break;
				}
            }
        } // end
	}

	public class Setter extends Thread {
		public void run() {
			while (true) {
				tc.setFoot(jT.getFoot());
				score1 = tc.getScore();
				scorejl.setText("점수 : " + score1);
				heart1 = tc.getHeart();
				heart.setText("목숨 : " + heart1);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		dispose();
	}
}
