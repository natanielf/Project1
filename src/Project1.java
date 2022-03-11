import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Project1 {

	private int rows, cols, rooms;
	private Tile[][] map;
	private Queue<int[]> enqueue, dequeue;
	private Stack<int[]> instack, outstack;
	private int[] kirby, cake;
	private boolean foundCake;
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
			this.foundCake = false;

			populateMap();

			findKirby();

			initQueue();
			queueMove();

			// initStack();
			// stackMove();

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
		p1.printQueuePath();
	}

	public void printQueuePath() {
		System.out.println("Solution (Queue):");
		Tile[][] solution = new Tile[this.rows][this.cols];
		int[] curr = null;
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
				solution[curr[0]][curr[1]].setValue('+');
			}
		}
		for (int r = 0; r < solution.length; r++) {
			for (int c = 0; c < solution[r].length; c++) {
				System.out.print(solution[r][c].getValue());
			}
			System.out.println();
		}
	}

	public void findKirby() {
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
		this.kirby = new int[] { kR, kC };
		map[kirby[0]][kirby[1]].setVisited(true);
	}

	public void initQueue() {
		this.enqueue = new Queue<>();
		this.dequeue = new Queue<>();
		enqueue.add(this.kirby);
	}

	public void initStack() {
		this.instack = new Stack<>();
		this.outstack = new Stack<>();
		instack.push(this.kirby);
	}

	public boolean moveIsValid(int row, int col) {
		return (isWalkable(row, col) && !map[row][col].isVisited());
	}

	public void queueMove() {
		int[] coordinates = enqueue.remove();
		dequeue.add(coordinates);
		int row = coordinates[0];
		int col = coordinates[1];

		map[row][col].setVisited(true);

		if (tileHoldsCake(row, col)) {
			foundCake = true;
			this.cake = new int[] { row, col };
		}

		if (!foundCake) {

			// North
			if (moveIsValid(row - 1, col)) {
				int[] newCoordinates = { row - 1, col };
				enqueue.add(newCoordinates);
			}

			// South
			if (moveIsValid(row + 1, col)) {
				int[] newCoordinates = { row + 1, col };
				enqueue.add(newCoordinates);
			}

			// East
			if (moveIsValid(row, col + 1)) {
				int[] newCoordinates = { row, col + 1 };
				enqueue.add(newCoordinates);
			}

			// West
			if (moveIsValid(row, col - 1)) {
				int[] newCoordinates = { row, col - 1 };
				enqueue.add(newCoordinates);
			}

			queueMove();
		}
	}

	public void stackMove() {
		int[] coordinates = instack.pop();
		outstack.push(coordinates);
		int row = coordinates[0];
		int col = coordinates[1];

		map[row][col].setVisited(true);

		if (tileHoldsCake(row, col)) {
			foundCake = true;
			this.cake = new int[] { row, col };
		}

		if (!foundCake) {

			// North
			if (moveIsValid(row - 1, col)) {
				int[] newCoordinates = { row - 1, col };
				instack.push(newCoordinates);
			}

			// South
			if (moveIsValid(row + 1, col)) {
				int[] newCoordinates = { row + 1, col };
				instack.push(newCoordinates);
			}

			// East
			if (moveIsValid(row, col + 1)) {
				int[] newCoordinates = { row, col + 1 };
				instack.push(newCoordinates);
			}

			// West
			if (moveIsValid(row, col - 1)) {
				int[] newCoordinates = { row, col - 1 };
				instack.push(newCoordinates);
			}

			stackMove();
		}
	}

	public boolean isWalkable(int r, int c) {
		if (r >= 0 && r < this.map.length) {
			if (c >= 0 && c < this.map[r].length) {
				return map[r][c].getValue() == '.' || map[r][c].getValue() == 'C';
			}
		}
		return false;
	}

	public boolean tileHoldsCake(int r, int c) {
		return map[r][c].getValue() == 'C';
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
			try (Scanner s = new Scanner(f)) {
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
			}
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
			try (Scanner s = new Scanner(f)) {
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
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void populateMap() {
		if (isTextBased)
			textBased();
		else
			coordinateBased();
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
