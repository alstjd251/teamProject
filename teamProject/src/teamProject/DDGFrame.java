package teamProject;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DDGFrame extends JFrame implements ActionListener {
	private JPanel gameMain;
	private JButton duduGame1;
	private JButton jumpGame;
	private JButton blockGame;
	private ImageIcon duduimg = new ImageIcon("C://projectImage_png/default_dudu.png");
	private ImageIcon duduimg2 = new ImageIcon("C://projectImage_png/over_dudu.png");
	private ImageIcon blockimg = new ImageIcon("C://projectImage_png/basic_block.png");
	private ImageIcon blockimg2 = new ImageIcon("C://projectImage_png/rollover_block.png");
	private ImageIcon jumpimg = new ImageIcon("C://projectImage_png/jump_button_icon.png");
	private ImageIcon jumpimg2 = new ImageIcon("C://projectImage_png/button_jump_rollover.png");
	
	DDGFrame() {
		Dimension rscreen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(600, 300);
		int rxpos = (int) (rscreen.getWidth() / 2 - getWidth() / 2);
		int rypos = (int) (rscreen.getHeight() / 2 - getHeight() / 2);
		setLocation(rxpos, rypos);
		setTitle("게임선택화면");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		gameMain = new JPanel();
		gameMain.setLayout(null);
		add(gameMain);
		duduGame1 = new JButton(duduimg);
		duduGame1.setRolloverIcon(duduimg2);
		jumpGame = new JButton(jumpimg);
		jumpGame.setRolloverIcon(jumpimg2);
		blockGame = new JButton(blockimg);
		blockGame.setRolloverIcon(blockimg2);

		duduGame1.setBorderPainted(false);
		jumpGame.setBorderPainted(false);
		blockGame.setBorderPainted(false);

		duduGame1.setBounds(0, 0, 200, 300);
		jumpGame.setBounds(199, 0, 200, 300);
		blockGame.setBounds(399, 0, 200, 300);

		duduGame1.addActionListener(this);
		jumpGame.addActionListener(this);
		blockGame.addActionListener(this);

		gameMain.add(duduGame1);
		gameMain.add(jumpGame);
		gameMain.add(blockGame);

		setResizable(false);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Dudu duduframe1 = new Dudu();
		if (e.getSource() == duduGame1) {
			if (duduframe1 == null) {
				duduframe1 = new Dudu();
				duduframe1.setVisible(true);
			} else {
				duduframe1.dispose();
				duduframe1 = new Dudu();
				duduframe1.setVisible(true);
			}
		} else if (e.getSource() == jumpGame) {
			JGame jumpframe = new JGame();
			if (jumpframe == null) {
				jumpframe = new JGame();
				jumpframe.setVisible(true);
			} else {
				jumpframe.dispose();
				jumpframe = new JGame();
				jumpframe.setVisible(true);
			}

		} else if (e.getSource() == blockGame) {
			BlockGame.MyFrame mf = new BlockGame.MyFrame();
			if (mf == null) {
				mf.setVisible(true);
			} else {
				mf.dispose();
				mf.setVisible(true);
			}
		}
	}

	public static void main(String args[]) {
		new DDGFrame();
	}
}