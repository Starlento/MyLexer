package LexPackage;

public class Ans implements Comparable<Ans> {
	private int x, y;
	private int start;
	private int length;
	private String content;
	private int id;

	public Ans() {
		start = -1;
		content = null;
		id = -1;
	}

	public Ans(int x, int y, int start, int length, String content, int id) {
		this.x = x;
		this.y = y;
		this.start = start;
		this.length = length;
		this.content = content;
		this.id = id;
	}

	public Ans(Ans t) {
		x = t.x;
		y = t.y;
		start = t.start;
		length = t.length;
		content = t.content;
		id = t.id;
	}

	public int compareTo(Ans o) {
		return this.start - o.start;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getStart() {
		return start;
	}

	public int getLength() {
		return length;
	}

	public String getContent() {
		return content;
	}

	public int getId() {
		return id;
	}

	public void printAns() {
		System.out.println("start:" + start + " length:" + length + " content:" + content + " id:" + id);
	}
}
