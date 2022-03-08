
public class Tile {

	private char value;
	private boolean visited;

	public Tile(char value, boolean visited) {
		this.value = value;
		this.visited = visited;
	}
	
	public Tile(char value) {
		this.value = value;
		this.visited = false;
	}

	public char getValue() {
		return value;
	}

	public void setValue(char value) {
		this.value = value;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

}
