import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Project1 {

	private int rows, cols, rooms;
	private Tile[][] map;
	private Queue<int[]> enqueue, dequeue;
	private int[] kirby, cake;
	private Scanner s;
	private boolean isTextBased;

	public Project1(File f) {
		try {
			this.s = new Scanner(f);
			this.rows = s.nextInt();
			this.cols = s.nextInt();
			this.rooms = s.nextInt();
			this.map = new Tile[rows][cols];
			this.s.nextLine();
			this.isTextBased = f.getPath().substring(0, f.getPath().length() - 4).contains("t");

			if (isTextBased)
				textBased();
			else
				coordinateBased();

			this.enqueue = new Queue<>();
			this.dequeue = new Queue<>();
			int kR = -1;
			int kC = -1;
			for (int r = 0; r < map.length; r++) {
				for (int c = 0; c < map[r].length; c++) {
					if (map[r][c].getValue() == 'K') {
						kR = r;
						kC = c;
					}
				}
			}
			kirby = new int[] { kR, kC };
			enqueue.add(kirby);
			map[kR][kC].setVisited(true);
			queueMove();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File f1 = new File("./maps/map1t.txt");
		System.out.println(f1.getPath());
		Project1 p1 = new Project1(f1);
		System.out.println(p1);
		System.out.println(p1.getCakeCoordinates());
		p1.printThePathFromKirbyToCakeAsAStringForQueueBasedPathfindingAlgorithm();

		// File f2 = new File("./maps/map1c.txt");
		// System.out.println(f2.getPath());
		// Project1 p2 = new Project1(f2);
		// System.out.println(p2);

		// System.out.println(Project1.textToCoordinate(f1));

		// System.out.println(Project1.coordinateToText(f2));
	}

	public void printThePathFromKirbyToCakeAsAStringForQueueBasedPathfindingAlgorithm() {
		System.out.println("Solution:");
		Tile[][] solution = new Tile[this.rows][this.cols];
		int[] curr = dequeue.remove();
		int[] next = dequeue.remove();
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				solution[r][c] = new Tile(map[r][c].getValue());
			}
		}
		for (int i = 0; i < dequeue.size() - 1; i++) {
			curr = next;
			next = dequeue.remove();
			if ((curr[0] == next[0] && curr[1] == next[1] || curr[1] == next[1] + 1 || curr[1] == next[1] - 1)
					|| (curr[1] == next[1] && curr[0] == next[0] || curr[0] == next[0] + 1 || curr[0] == next[0] - 1)) {
				{
					solution[curr[0]][curr[1]].setValue('+');
				}
			}
		}
		for (int r = 0; r < solution.length; r++) {
			for (int c = 0; c < solution[r].length; c++) {
				System.out.print(solution[r][c].getValue());
			}
			System.out.println();
		}
	}

	public void queueMove() {
		int[] coordinates = enqueue.remove();
		int row = coordinates[0];
		int col = coordinates[1];
		dequeue.add(coordinates);

		// North
		if (isWalkable(row - 1, col) && !map[row - 1][col].isVisited()) {
			int[] newCoordinates = { row - 1, col };
			this.enqueue.add(newCoordinates);
			map[row - 1][col].setVisited(true);
			checkCake(row - 1, col);
			if (this.cake != null) {
				return;
			}
		}

		// South
		if (isWalkable(row + 1, col) && !map[row + 1][col].isVisited()) {
			int[] newCoordinates = { row + 1, col };
			this.enqueue.add(newCoordinates);
			map[row + 1][col].setVisited(true);
			checkCake(row + 1, col);
			if (this.cake != null) {
				return;
			}
		}

		// East
		if (isWalkable(row, col + 1) && !map[row][col + 1].isVisited()) {
			int[] newCoordinates = { row, col + 1 };
			this.enqueue.add(newCoordinates);
			map[row][col + 1].setVisited(true);
			checkCake(row, col + 1);
			if (this.cake != null) {
				return;
			}
		}

		// West
		if (isWalkable(row, col - 1) && !map[row][col - 1].isVisited()) {
			int[] newCoordinates = { row, col - 1 };
			this.enqueue.add(newCoordinates);
			map[row][col - 1].setVisited(true);
			checkCake(row, col - 1);
			if (this.cake != null) {
				return;
			}
		}

		if (this.cake == null) {
			{
				int[] next = { row - 1, col };
				enqueue.add(next);
				queueMove();
			}
			{
				int[] next = { row + 1, col };
				enqueue.add(next);
				queueMove();
			}
			{
				int[] next = { row, col + 1 };
				enqueue.add(next);
				queueMove();
			}
			{
				int[] next = { row, col - 1 };
				enqueue.add(next);
				queueMove();
			}
		}
	}

	public boolean isWalkable(int r, int c) {
		if (r >= 0 && r < this.map.length) {
			if (c >= 0 && c < this.map.length) {
				return true;
			}
		}
		return false;
	}

	public void checkCake(int r, int c) {
		if (map[r][c].getValue() == 'C') {
			cake = new int[2];
			this.cake[0] = r;
			this.cake[1] = c;
		}
	}

	public String getCakeCoordinates() {
		return "Cake: row " + this.cake[0] + ", column " + this.cake[1];
	}

	public void textBased() {
		int r = 0;
		while (s.hasNextLine()) {
			char[] line = s.nextLine().toCharArray();
			for (int c = 0; c < line.length; c++) {
				map[r][c] = new Tile(line[c]);
			}
			r++;
		}
	}

	public void coordinateBased() {
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				map[r][c] = new Tile('.');
			}
		}
		while (s.hasNextLine()) {
			char[] line = s.nextLine().toCharArray();
			int row = Character.getNumericValue(line[2]);
			int col = Character.getNumericValue(line[4]);
			map[row][col].setValue(line[0]);
		}
	}

	public static String textToCoordinate(File f) {
		boolean isTextBased = f.getPath().substring(0, f.getPath().length() - 4).contains("t");
		if (!isTextBased)
			return null;

		try {
			Scanner s = new Scanner(f);
			String str = "";
			str += s.nextLine() + "\n";
			int row = 0;
			while (s.hasNextLine()) {
				char[] line = s.nextLine().toCharArray();
				for (int i = 0; i < line.length; i++) {
					char curr = line[i];
					if (curr != '.') {
						str += Character.toString(curr) + " " + row + " " + i;
						str += "\n";
					}
				}
				row++;
			}
			return str;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String coordinateToText(File f) {
		boolean isTextBased = f.getPath().substring(0, f.getPath().length() - 4).contains("t");
		if (isTextBased)
			return null;

		try {
			Scanner s = new Scanner(f);
			String str = "";
			int rows = s.nextInt();
			int cols = s.nextInt();
			int rooms = s.nextInt();
			char[][] map = new char[rows][cols];
			s.nextLine();
			for (int r = 0; r < map.length; r++) {
				for (int c = 0; c < map[r].length; c++) {
					map[r][c] = '.';
				}
			}
			while (s.hasNextLine()) {
				char[] line = s.nextLine().toCharArray();
				int row = Character.getNumericValue(line[2]);
				int col = Character.getNumericValue(line[4]);
				map[row][col] = line[0];
			}
			str += rows + " " + cols + " " + rooms + "\n";
			for (int r = 0; r < map.length; r++) {
				for (int c = 0; c < map[r].length; c++) {
					str += map[r][c];
				}
				str += "\n";
			}
			return str;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String toString() {
		String str = "";
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				str += map[r][c].getValue();
			}
			str += "\n";
		}
		return str;
	}

}
