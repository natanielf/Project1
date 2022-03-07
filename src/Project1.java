import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Project1 {

	private int rows, cols, rooms;
	private char[][] map;
	private boolean[][] visited;
	private Queue<int[]> enqueue, dequeue;
	private int[] cake;
	private Scanner s;
	private boolean isTextBased;

	public Project1(File f) {
		try {
			this.s = new Scanner(f);
			this.rows = s.nextInt();
			this.cols = s.nextInt();
			this.rooms = s.nextInt();
			this.map = new char[rows][cols];
			this.s.nextLine();
			this.visited = new boolean[rows][cols];
			resetVisited();
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
					if (map[r][c] == 'K') {
						kR = r;
						kC = c;
					}
				}
			}
			int[] coordinates = { kR, kC };
			enqueue.add(coordinates);
			addVisit(kR, kC);
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
		// p1.getCakeCoordinates();

		// File f2 = new File("./maps/map1c.txt");
		// System.out.println(f2.getPath());
		// Project1 p2 = new Project1(f2);
		// System.out.println(p2);

		// System.out.println(Project1.textToCoordinate(f1));

		// System.out.println(Project1.coordinateToText(f2));
	}

	public void queueMove() {
		int[] coordinates = enqueue.remove();
		int row = coordinates[0];
		int col = coordinates[1];
		dequeue.add(coordinates);

		// North
		if (isWalkable(row - 1, col) && !isVisited(row - 1, col)) {
			int[] newCoordinates = { row - 1, col };
			this.enqueue.add(newCoordinates);
			addVisit(row - 1, col);
			checkCake(row - 1, col);
			System.out.println("N");
		}

		// South
		if (isWalkable(row + 1, col) && !isVisited(row + 1, col)) {
			int[] newCoordinates = { row + 1, col };
			this.enqueue.add(newCoordinates);
			addVisit(row + 1, col);
			checkCake(row + 1, col);
			System.out.println("S");
		}

		// East
		if (isWalkable(row, col + 1) && !isVisited(row, col + 1)) {
			int[] newCoordinates = { row, col + 1 };
			this.enqueue.add(newCoordinates);
			addVisit(row, col + 1);
			checkCake(row, col + 1);
			System.out.println("E");
		}

		// West
		if (isWalkable(row, col - 1) && !isVisited(row, col - 1)) {
			int[] newCoordinates = { row, col - 1 };
			this.enqueue.add(newCoordinates);
			addVisit(row, col - 1);
			checkCake(row, col - 1);
			System.out.println("W");
		}

		if (this.cake == null) {
			if (isWalkable(row - 1, col) && !isVisited(row - 1, col)) {
				queueMove(row - 1, col);
			}
			if (isWalkable(row + 1, col) && !isVisited(row + 1, col)) {
				queueMove(row + 1, col);
			}
			if (isWalkable(row, col + 1) && !isVisited(row, col + 1)) {
				queueMove(row, col + 1);
			}
			if (isWalkable(row, col - 1) && !isVisited(row, col - 1)) {
				queueMove(row, col - 1);
			}
		} else {
			System.out.println("Cake - Row: " + this.cake[0] + " Column: " + this.cake[1]);
		}
	}

	public void queueMove(int row, int col) {
		// North
		if (isWalkable(row - 1, col) && !isVisited(row - 1, col)) {
			int[] newCoordinates = { row - 1, col };
			this.enqueue.add(newCoordinates);
			addVisit(row - 1, col);
			checkCake(row - 1, col);
		}

		// South
		if (isWalkable(row + 1, col) && !isVisited(row + 1, col)) {
			int[] newCoordinates = { row + 1, col };
			this.enqueue.add(newCoordinates);
			addVisit(row + 1, col);
			checkCake(row + 1, col);
		}

		// East
		if (isWalkable(row, col + 1) && !isVisited(row, col + 1)) {
			int[] newCoordinates = { row, col + 1 };
			this.enqueue.add(newCoordinates);
			addVisit(row, col + 1);
			checkCake(row, col + 1);
		}

		// West
		if (isWalkable(row, col - 1) && !isVisited(row, col - 1)) {
			int[] newCoordinates = { row, col - 1 };
			this.enqueue.add(newCoordinates);
			addVisit(row, col - 1);
			checkCake(row, col - 1);
		}

		if (cake == null) {
			if (isWalkable(row - 1, col) && !isVisited(row - 1, col)) {
				queueMove(row - 1, col);
			}
			if (isWalkable(row + 1, col) && !isVisited(row + 1, col)) {
				queueMove(row + 1, col);
			}
			if (isWalkable(row, col + 1) && !isVisited(row, col + 1)) {
				queueMove(row, col + 1);
			}
			if (isWalkable(row, col - 1) && !isVisited(row, col - 1)) {
				queueMove(row, col - 1);
			}
		}

	}

	public void resetVisited() {
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				this.visited[r][c] = false;
			}
		}
	}

	public void addVisit(int r, int c) {
		this.visited[r][c] = true;
	}

	public boolean isVisited(int r, int c) {
		return this.visited[r][c];
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
		if (map[r][c] == 'C') {
			cake = new int[2];
			this.cake[0] = r;
			this.cake[1] = c;
		}
	}

	public String getCakeCoordinates() {
		return "Row: " + this.cake[0] + " Column" + this.cake[1];
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
			int row = Character.getNumericValue(line[2]);
			int col = Character.getNumericValue(line[4]);
			map[row][col] = line[0];
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
				str += map[r][c];
			}
			str += "\n";
		}
		return str;
	}

}
