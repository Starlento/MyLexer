package LexPackage;

import java.io.PrintStream;

public class Res extends Ans {
	private int groupnumber;
	private int position;

	public Res() {
		groupnumber = -1;
		position = -1;
	}

	public Res(Ans t, int groupnumber, int position) {
		super(t);
		this.groupnumber = groupnumber;
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public int getGroupnumber() {
		return groupnumber;
	}

	public void printRes(PrintStream p) {
		p.println("line:" + super.getX() + " row:" + super.getY() + " (" + groupnumber + "," + position + ")" + " "
				+ super.getContent());
	}
}
