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
	private char target;
	private int roomsVisited;
	private File f;
	private Scanner s;
	private boolean queueApproach, stackApproach, optimalApproach;
	private boolean isTextBased, printTextBased, printTime;
	private long startTime, endTime;

	public p1() {
		// Implementation not shown ;)
	}

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
		this.printTime = false;
		checkCLIArguments(args);

		this.rows = s.nextInt();
		this.cols = s.nextInt();
		this.rooms = s.nextInt();

		checkMapParameters();

		this.map = new Tile[rows * rooms][cols];
		this.s.nextLine();
		this.foundCake = false;
		this.target = 'C';
		this.roomsVisited = 0;
		this.kirby = new int[2];

		populateMap();

		this.startTime = System.currentTimeMillis();

		if (queueApproach) {
			findKirby();
			initQueue();

			while (roomsVisited < rooms) {
				if ((rooms - roomsVisited) == 1)
					target = 'C';
				else
					target = '|';

				queueMove();
				roomsVisited++;
				if (roomsVisited < rooms) {
					findKirby();
					enqueue.add(kirby);
				}
			}

			this.endTime = System.currentTimeMillis();

			if (!foundCake)
				System.out.println("The cake is a lie.");
			else
				printQueuePath();

			if (printTime)
				printTime();

		} else if (stackApproach) {
			findKirby();
			initStack();

			while (roomsVisited < rooms) {
				if ((rooms - roomsVisited) == 1)
					target = 'C';
				else
					target = '|';

				stackMove();
				roomsVisited++;
				if (roomsVisited < rooms) {
					findKirby();
					instack.push(kirby);
				}
			}

			this.endTime = System.currentTimeMillis();

			if (!foundCake)
				System.out.println("The cake is a lie.");
			else
				printStackPath();

			if (printTime)
				printTime();

		} else {
			// optimal approach
			this.endTime = System.currentTimeMillis();
			if (printTime) {
				printTime();
			}
		}

	}

	public static void main(String[] args) {
		new p1(args);
	}

	public void printQueuePath() {
		Tile[][] solution = map.clone();

		if (printTextBased) {
			while (dequeue.size() > 0) {
				int[] curr = dequeue.remove();
				int row = curr[0];
				int col = curr[1];
				char val = solution[row][col].getValue();

				if (val != 'K' && val != 'C' && val != '|') {
					solution[curr[0]][curr[1]].setValue('+');
				}
			}
			System.out.println(toString(solution));
		} else {
			String str = "";
			while (dequeue.size() > 0) {
				int[] curr = dequeue.remove();
				int row = curr[0];
				int col = curr[1];
				str += "+ " + row + " " + col + "\n";
			}
			System.out.println(str);
		}
	}

	public void printStackPath() {
		Tile[][] solution = map.clone();

		if (printTextBased) {
			while (outstack.size() > 0) {
				int[] curr = outstack.pop();
				int row = curr[0];
				int col = curr[1];
				char val = solution[row][col].getValue();

				if (val != 'K' && val != 'C' && val != '|') {
					solution[curr[0]][curr[1]].setValue('+');
				}
			}
			System.out.println(toString(solution));
		} else {
			while (outstack.size() > 0) {
				int[] curr = outstack.pop();
				int row = curr[0];
				int col = curr[1];
				System.out.println("+ " + row + " " + col);
			}
		}
	}

	public void findKirby() {
		for (int r = 0; r < map.length; r++) {
			for (int c = 0; c < map[r].length; c++) {
				if (map[r][c].getValue() == 'K') {
					if (!map[r][c].isVisited()) {
						this.kirby[0] = r;
						this.kirby[1] = c;
						map[r][c].setVisited(true);
						return;
					}
				}
			}
		}
		System.err.println("Error: Cannot find Kirby.");
		System.exit(-1);
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
		if (enqueue.size() <= 0)
			return;

		int[] coordinates = enqueue.remove();
		int row = coordinates[0];
		int col = coordinates[1];

		dequeue.add(coordinates);
		map[row][col].setVisited(true);

		if (tileHoldsCake(row, col)) {
			foundCake = true;
			cake = new int[] { row, col };
		}

		if (!foundCake) {

			// North
			if (moveIsValid(row - 1, col))
				enqueue.add(new int[] { row - 1, col });

			// South
			if (moveIsValid(row + 1, col))
				enqueue.add(new int[] { row + 1, col });

			// East
			if (moveIsValid(row, col + 1))
				enqueue.add(new int[] { row, col + 1 });

			// West
			if (moveIsValid(row, col - 1))
				enqueue.add(new int[] { row, col - 1 });

			queueMove();
		}
	}

	public void stackMove() {
		if (instack.size() <= 0)
			return;

		int[] coordinates = instack.pop();
		int row = coordinates[0];
		int col = coordinates[1];

		outstack.push(coordinates);
		map[row][col].setVisited(true);

		if (tileHoldsCake(row, col)) {
			foundCake = true;
			cake = new int[] { row, col };
		}

		if (!foundCake) {

			// North
			if (moveIsValid(row - 1, col))
				instack.push(new int[] { row - 1, col });

			// South
			if (moveIsValid(row + 1, col))
				instack.push(new int[] { row + 1, col });

			// East
			if (moveIsValid(row, col + 1))
				instack.push(new int[] { row, col + 1 });

			// West
			if (moveIsValid(row, col - 1))
				instack.push(new int[] { row, col - 1 });

			stackMove();
		}
	}

	public boolean isWalkable(int r, int c) {
		if (r >= 0 && r < this.map.length) {
			if (c >= 0 && c < this.map[r].length) {
				return map[r][c].getValue() == '.' || map[r][c].getValue() == 'C' || map[r][c].getValue() == '|';
			}
		}
		return false;
	}

	public boolean tileHoldsTarget(int r, int c) {
		return map[r][c].getValue() == target;
	}

	public boolean tileHoldsCake(int r, int c) {
		return map[r][c].getValue() == 'C';
	}

	public String getCakeCoordinates() {
		return "Cake: row " + this.cake[0] + ", column " + this.cake[1];
	}

	public void textBased() {
		int r = 0;
		while (s.hasNextLine() && r < map.length) {
			char[] line = s.nextLine().toCharArray();
			if (line.length != cols) {
				System.err.println("Error: Invalid column size provided.");
				System.err.println("The map has " + line.length + " columns, but should have " + cols + " columns.");
				System.exit(-1);
			}
			for (int c = 0; c < line.length; c++) {
				if (line[c] != '.' && line[c] != '@' && line[c] != '|' && line[c] != 'K' && line[c] != 'C') {
					System.err.println("Error: Invalid map character found.");
					System.err.println("Legal map charaters: '.', '@', '|', 'K', 'C'. " + line[c] + " was found.");
					System.exit(-1);
				} else
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
			String[] line = s.nextLine().trim().split(" ");
			char value = line[0].toCharArray()[0];
			int row = Integer.parseInt(line[1]);
			int col = Integer.parseInt(line[2]);
			if (row < 0 || row >= rows) {
				System.err.println("Error: Invalid row coordinate provided.");
				System.err.println("The map has " + rows + " rows, but row " + row + " was found.");
				System.exit(-1);
			} else if (col < 0 || col >= cols) {
				System.err.println("Error: Invalid column coordinate provided.");
				System.err.println("The map has " + cols + " columns, but column " + col + " was found.");
				System.exit(-1);
			} else if (value != '.' && value != '@' && value != '|' && value != 'K' && value != 'C') {
				System.err.println("Error: Invalid map character found.");
				System.err.println("Legal map charaters: '.', '@', '|', 'K', 'C'. " + value + " was found.");
				System.exit(-1);
			} else
				map[row][col].setValue(value);
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
		this.queueApproach = false;
		this.stackApproach = false;
		this.optimalApproach = false;
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
			} else if (arg.equals("--Time")) {
				this.printTime = true;
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

	public void checkMapParameters() {
		if (rows <= 0 || cols <= 0 || rooms <= 0) {
			System.err.println("Error: Invalid map parameters. Maps must have more than 0 rows, columns, and rooms.");
			System.exit(-1);
		}
	}

	public void printTime() {
		System.out.println("Total Runtime: " + millisToSec(endTime - startTime) + " seconds");
	}

	public double millisToSec(long ms) {
		return (double) (ms / 1000.0);
	}
}
