package teamProject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import com.sun.tools.javac.Main;

import javazoom.jl.player.Player;

public class BlockMusic extends Thread {

	private Player player;
	private boolean isLoop; // 한 곡 반복할건지 끌건지
	private File file;
	private FileInputStream fis;
	private BufferedInputStream bis;

	public BlockMusic(String name, boolean isLoop) {
		try {
			this.isLoop = isLoop;
			file = new File("../teamProject/music/Tristan Lohengrin - Happy 8bit Loop 01.mp3");
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			player = new Player(bis);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void close() {
		isLoop = false;
		player.close(); // 음악 종료
		this.stop(); // thread 중지상태(종료)
	}

	public void run() {
		try {
			do {
				player.play();
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				player = new Player(bis);
			} while (isLoop);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
