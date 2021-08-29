package teamProject;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class DuduRecord extends JFrame {
	private JTextField user;
	private JLabel userName;
	private JLabel jscore;
	private JButton re;
	private JButton rank;
	private int dscore;
	private JTextArea scoretable;
	int checkGame;

	DuduRecord(int dscore, int checkGame) {
		setAlwaysOnTop(true);
		this.checkGame = checkGame;
		this.dscore = dscore;
		Dimension rscreen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(300, 500);
		int rxpos = (int) (rscreen.getWidth() / 2 - getWidth() / 2);
		int rypos = (int) (rscreen.getHeight() / 2 - getHeight() / 2);
		setLocation(rxpos, rypos);
		setTitle("record");
		this.setLayout(null);
		// setDefaultCloseOperation(EXIT_ON_CLOSE);
		user = new JTextField();
		user.setBounds(80, 10, 110, 30);
		userName = new JLabel("UserName : ");
		userName.setBounds(10, 10, 110, 30);

		jscore = new JLabel("score : " + String.valueOf(dscore));
		jscore.setBounds(10, 50, 110, 30);

		re = new JButton("���");
		re.setBounds(200, 10, 70, 30);

		scoretable = new JTextArea();
		scoretable.setBounds(10, 100, 265, 350);

		rank = new JButton("���� ����");
		rank.setBounds(200, 50, 70, 30);

		add(user);
		add(userName);
		add(jscore);
		add(re);
		add(scoretable);
		add(rank);
		setResizable(false);
		setVisible(true);

		DAO dao = new DAO(checkGame);
		re.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scoretable.setText("");
				String name = user.getText();
				int score = dscore;
				dao.insertData(new Data(name, score));
				scoretable.append("�Է� �Ϸ�\n");
				user.setText("");
			}
		});

		rank.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				scoretable.setText("");
				ArrayList<Data> arr = new ArrayList<Data>();
				arr = dao.readData();
				scoretable.append("id" + "\t" + "score" + "\n");
				scoretable.append("===================================\n");
				for (int i = 0; i < arr.size(); i++) {
					scoretable.append(arr.get(i).getName() + " \t" + arr.get(i).getScore() + "\n");
				}
			}
		});
	}
}