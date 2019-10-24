package LexPackage;

public class Lex {
	private String format;
	private int id;
	private String name;

	public Lex() {
		id = -1;
		format = null;
		name = null;
	}

	public Lex(String format, int id, String name) {
		this.id = id;
		this.format = format;
		this.name = name;
	}

	public String getFormat() {
		return format;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void printLex() {
		System.out.println("format:" + format + " id:" + id + " name:" + name);
	}
}
