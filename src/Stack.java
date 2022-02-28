import java.util.ArrayList;

public class Stack<T> {

	private ArrayList<T> data;
	private int size;

	public Stack() {
		this.data = new ArrayList<T>();
		this.size = 0;
	}

	public int size() {
		return size;
	}

	public void push(T el) {
		data.add(0, el);
		size++;
	}

	public T pop() {
		if (size > 0) {
			size--;
			return data.remove(0);
		} else
			return null;
	}

	public String toString() {
		String str = "[ ";
		for (int i = 0; i < data.size(); i++) {
			str += data.get(i);
			if (i < data.size() - 1)
				str += ", ";
		}
		str += " ]";
		return str;
	}

	public void print() {
		System.out.println(toString());
	}

}
