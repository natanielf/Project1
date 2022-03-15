import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class p1 {

	private int rows, cols, rooms;
	private Tile[][] map;
	private Queue<int[]> enqueue, dequeue;
	private Stack<int[]> instack, outstack;
	private int[] kirby, cake;
	private boolean foundCake;
	private File f;
	private Scanner s;
	private boolean queueApproach, stackApproach, optimalApproach;
	private boolean isTextBased, printTextBased;

	public p1(File f) {
		try {
			this.s = new Scanner(f);
			this.rows = s.nextInt();
			this.cols = s.nextInt();
			this.rooms = s.nextInt();
			this.map = new Tile[rows][cols];
			this.s.nextLine();
			this.isTextBased = f.getPath().substring(0, f.getPath().length() - 4).contains("t");
			this.printTextBased = true;
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

	public p1(String[] args) {
		this.printTextBased = true;
		this.isTextBased = true;
		checkCLIArguments(args);

		this.rows = s.nextInt();
		this.cols = s.nextInt();
		this.rooms = s.nextInt();
		this.map = new Tile[rows][cols];
		this.s.nextLine();
		this.foundCake = false;

		populateMap();

		findKirby();

		if (queueApproach) {
			initQueue();
			queueMove();
			if (printTextBased) {
				printQueuePathTextBased();
			}
		} else if (stackApproach) {
			initStack();
			stackMove();
		} else {
			// optimal approach
		}

	}

	public static void main(String[] args) {
		p1 p = new p1(args);
	}

	public void printQueuePathTextBased() {
		Tile[][] solution = new Tile[rows][cols];
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				solution[r][c] = new Tile(map[r][c].getValue());
			}
		}
		int[] curr = null;
		int[] next = dequeue.remove();
		while (dequeue.size() > 0) {
			curr = next;
			next = dequeue.remove();
			if ((curr[0] != kirby[0] || curr[1] != kirby[1]) && (curr[0] != cake[0] || curr[1] != cake[1])) {
				if (!solution[curr[0]][curr[1]].isVisited()) {
					if ((curr[0] == next[0] && curr[1] == next[1] || curr[1] == next[1] + 1 || curr[1] == next[1] - 1)
							|| (curr[1] == next[1] && curr[0] == next[0] || curr[0] == next[0] + 1
									|| curr[0] == next[0] - 1)) {
						solution[curr[0]][curr[1]].setValue('+');
						solution[curr[0]][curr[1]].setVisited(true);
					}
				}

			}

		}
		System.out.println(toString(solution));
	}

	public void printQueuePathTextBased1() {
		Tile[][] solution = new Tile[rows][cols];
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				solution[r][c] = new Tile(map[r][c].getValue());
			}
		}

		while (dequeue.size() > 0) {
			int[] curr = dequeue.remove();
			if ((curr[0] != kirby[0] || curr[1] != kirby[1]) && (curr[0] != cake[0] || curr[1] != cake[1])) {
				solution[curr[0]][curr[1]].setValue('+');
			}
		}
		System.out.println(toString(solution));
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
		map[kR][kC].setVisited(true);
	}

	public void initQueue() {
		this.enqueue = new Queue<>();
		this.dequeue = new Queue<>();
		enqueue.add(kirby);
	}

	public void initStack() {
		this.instack = new Stack<>();
		this.outstack = new Stack<>();
		instack.push(kirby);
	}

	public boolean moveIsValid(int row, int col) {
		return (isWalkable(row, col) && !map[row][col].isVisited());
	}

	public void queueMove() {
		int[] coordinates = enqueue.remove();
		dequeue.add(coordinates);
		int row = coordinates[0];
		int col = coordinates[1];

		if (tileHoldsCake(row, col)) {
			foundCake = true;
			cake = new int[] { row, col };
		}

		map[row][col].setVisited(true);

		if (!foundCake) {

			// North
			if (moveIsValid(row - 1, col)) {
				int[] newCoordinates = { row - 1, col };
				map[row - 1][col].setVisited(true);
				enqueue.add(newCoordinates);
			}

			// South
			if (moveIsValid(row + 1, col)) {
				int[] newCoordinates = { row + 1, col };
				map[row + 1][col].setVisited(true);
				enqueue.add(newCoordinates);
			}

			// East
			if (moveIsValid(row, col + 1)) {
				map[row][col + 1].setVisited(true);
				int[] newCoordinates = { row, col + 1 };
				enqueue.add(newCoordinates);
			}

			// West
			if (moveIsValid(row, col - 1)) {
				int[] newCoordinates = { row, col - 1 };
				map[row][col - 1].setVisited(true);
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

		if (tileHoldsCake(row, col)) {
			foundCake = true;
			this.cake = new int[] { row, col };
		}

		map[row][col].setVisited(true);

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

	public String toString(Tile[][] map) {
		String str = "";
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				str += map[r][c].getValue();
			}
			str += "\n";
		}
		return str;
	}

	public void checkCLIArguments(String[] args) {
		int numApproachArgs = 0;
		boolean displayHelp = false;
		for (String arg : args) {
			if (arg.equals("--Queue")) {
				this.queueApproach = true;
				numApproachArgs++;
			} else if (arg.equals("--Stack")) {
				this.stackApproach = true;
				numApproachArgs++;
			} else if (arg.equals("--Opt")) {
				this.optimalApproach = true;
				numApproachArgs++;
			} else if (arg.equals("--Incoordinate")) {
				this.isTextBased = false;
			} else if (arg.equals("--Outcoordinate")) {
				this.printTextBased = false;
			} else if (arg.equals("--Help")) {
				displayHelp = true;
			}
		}
		if (displayHelp) {
			String[] helpText = { "Help Kirby find the cake!", "Arguments:", "--Stack: Use the stack-based approach.",
					"--Queue: Find the shortest path,", "--Time: Print the runtime of the program as described above.",
					"--Incoordinate: The input file is in the coordinate-based system. If the switch is not set, the input file is in the text-map format.",
					"--Outcoordinate: The output file is in the coordinate-based system. If the switch is not set, the output file is in the text-map format.",
					"--Help: Displays this message and then exits." };
			for (String line : helpText) {
				System.out.println(line);
			}
			System.exit(0);
		}
		if (numApproachArgs != 1) {
			System.err.println("Error: Invalid arguments supplied.");
			System.err.println("Legal command line inputs must include exactly one of --Stack, --Queue, or --Opt.");
			System.exit(-1);
		}
		if (args.length > 0) {
			try {
				this.f = new File(args[args.length - 1]);
			} catch (Exception e) {
				System.err.println("Error: File not found.");
				e.printStackTrace();
			}

			try {
				this.s = new Scanner(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
