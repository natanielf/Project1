
public class Queue<T> {

	private Stack<T> a, b;
	private int size;

	public Queue() {
		this.a = new Stack<>();
		this.b = new Stack<>();
		this.size = 0;
	}

	public T remove() {
		if (size == 0)
			return null;
		size--;
		int len = a.size();
		for (int i = 0; i < len; i++) {
			b.push(a.pop());
		}
		return b.pop();
	}

	public void add(T el) {
		a.push(el);
		size++;
	}

	public int size() {
		return this.size;
	}

	public String toString() {
		if (a.size() == 0)
			return this.b.toString();
		else
			return this.a.toString();
	}

}
