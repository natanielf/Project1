import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class p1 {

	private int rows, cols, rooms;
	private char[][] map;
	private Scanner s;
	private boolean isTextBased;

	public p1(File f) {
		try {
			this.s = new Scanner(f);
			this.rows = s.nextInt();
			this.cols = s.nextInt();
			this.rooms = s.nextInt();
			this.map = new char[rows][cols];
			this.s.nextLine();
			this.isTextBased = f.getPath().substring(0, f.getPath().length() - 4).contains("t");

			if (isTextBased)
				textBased();
			else
				coordinateBased();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File f1 = new File("./maps/map1t.txt");
		System.out.println(f1.getPath());
		p1 p1 = new p1(f1);
		System.out.println(p1);

		File f2 = new File("./maps/map1c.txt");
		System.out.println(f2.getPath());
		p1 p2 = new p1(f2);
		System.out.println(p2);
	}

	public void textBased() {
		int r = 0;
		while (s.hasNextLine()) {
			char[] line = s.nextLine().toCharArray();
			for (int c = 0; c < line.length; c++) {
				map[r][c] = line[c];
			}
			r++;
		}
	}

	public void coordinateBased() {
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				map[r][c] = '.';
			}
		}
		while (s.hasNextLine()) {
			char[] line = s.nextLine().toCharArray();
			map[line[2]][line[4]] = line[0];
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				str += map[r][c];
			}
			str += "\n";
		}
		return str;
	}

}
