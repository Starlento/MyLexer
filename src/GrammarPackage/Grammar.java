package GrammarPackage;
import java.util.Vector;
import javax.swing.tree.DefaultMutableTreeNode;
import LexPackage.Res;

public class Grammar {
	private Vector<Res> result;
	private int i;
	private DefaultMutableTreeNode root;
	private boolean flag;
	String error="no error";
	boolean isProgram() {
		int count = 1;
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("<ChildProgram" + String.valueOf(count++) + ">");
		boolean judge = isChildProgram(tr);// 子程序
		if(!judge) {
			error="error start from " + result.get(i).getContent() + " in line:" + result.get(i).getX() + " row:"
					+ result.get(i).getY();
			return false;
		}
		root.add(tr);
		tr = new DefaultMutableTreeNode("<ChildProgram" + String.valueOf(count++) + ">");
		while (isChildProgram(tr)) {// 子程序
			root.add(tr);
			tr = new DefaultMutableTreeNode("<ChildProgram" + String.valueOf(count++) + ">");
		}
		if (result.size() == i && judge)// 必须已经将二元式读完
			return true;
		else if (judge) {
			error="start from " + result.get(i).getContent() + " in line:" + result.get(i).getX() + " row:"
					+ result.get(i).getY() + " is useless";
			return false;
		} else {
			return false;
		}
	}

	boolean isChildProgram(DefaultMutableTreeNode t) {
		int temp = i;
		DefaultMutableTreeNode tr;

		if (result.size() == i)
			return false;
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 0) {// procedure
			tr = new DefaultMutableTreeNode("procedure");
			t.add(tr);
			i++;
		} else {
			return false;
		}

		if (result.size() == i) {
			return false;
		}
		if (result.get(i).getGroupnumber() == 2) {// 标识符
			tr = new DefaultMutableTreeNode("\"" + result.get(i).getContent() + "\"");
			t.add(tr);
			i++;
		} else {
			return false;
		}

		if (result.size() == i) {
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 6) {// begin
			tr = new DefaultMutableTreeNode("begin");
			t.add(tr);
			i++;
		} else {
			return false;
		}

		tr = new DefaultMutableTreeNode("<SentenceList>");
		if (isSentenceList(tr)) {// 语句列表
			t.add(tr);
		} else {
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 7) {// end
			tr = new DefaultMutableTreeNode("end");
			t.add(tr);
			i++;
		} else {
			return false;
		}
		
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 29) {// .
			tr = new DefaultMutableTreeNode(".");
			t.add(tr);
			i++;
		} else {
			return false;
		}

		return true;
	}

	boolean isSentenceList(DefaultMutableTreeNode t) {
		int count = 1;
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("<sentence" + String.valueOf(count++) + ">");
		isSentence(tr);// 语句
		t.add(tr);

		if (result.size() == i)
			return true;
		while (result.get(i).getGroupnumber() == 15) {// ;
			tr = new DefaultMutableTreeNode(";");
			t.add(tr);
			i++;
			tr = new DefaultMutableTreeNode("<sentence" + String.valueOf(count++) + ">");
			isSentence(tr);// 语句
			t.add(tr);
			if (result.size() == i)
				return true;
		}
		return true;
	}

	boolean isSentence(DefaultMutableTreeNode t) {
		if (isVarSentence(t)) {
			return true;
		} else if (isAssignSentence(t)) {
			return true;
		} else if (isIfSentence(t)) {
			return true;
		} else if (isWhileSentence(t)) {
			return true;
		} else if (isCallSentence(t)) {
			return true;
		} else if (isComplexSentence(t)) {
			return true;
		}
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("ε");
		t.add(tr);
		return true;
	}

	boolean isVarSentence(DefaultMutableTreeNode f) {
		int temp = i;
		DefaultMutableTreeNode tr, t = new DefaultMutableTreeNode("<VarSentence>");
		if (result.size() == i) {
			System.out.println("ArrayOutOfBorder");
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 1) {// def
			i++;
			tr = new DefaultMutableTreeNode("def");
			t.add(tr);
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 2) {// 标识符
			tr = new DefaultMutableTreeNode("\"" + result.get(i).getContent() + "\"");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		while (result.get(i).getGroupnumber() == 16) {// ,
			tr = new DefaultMutableTreeNode(",");
			t.add(tr);
			i++;

			if (result.size() == i) {
				i = temp;
				return false;
			}
			if (result.get(i).getGroupnumber() == 2) {// 标识符
				tr = new DefaultMutableTreeNode("\"" + result.get(i).getContent() + "\"");
				t.add(tr);
				i++;
			} else {
				//outputError(i-1);
				i = temp;
				return false;
			}

			if (result.size() == i) {
				i = temp;
				return false;
			}
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		f.add(t);
		return true;
	}

	boolean isAssignSentence(DefaultMutableTreeNode f) {
		int temp = i;
		DefaultMutableTreeNode tr, t = new DefaultMutableTreeNode("<AssignSentence>");
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 2) {// 标识符
			tr = new DefaultMutableTreeNode("\"" + result.get(i).getContent() + "\"");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 8) {// =
			tr = new DefaultMutableTreeNode("=");
			t.add(tr);
			i++;
		} else {
		//	outputError(i-1);
			i = temp;
			return false;
		}

		tr = new DefaultMutableTreeNode("<Expression>");
		if (isExpression(tr)) {
			t.add(tr);
			f.add(t);
			return true;
		}
		return false;
	}

	boolean isIfSentence(DefaultMutableTreeNode f) {
		int temp = i;
		DefaultMutableTreeNode tr, t = new DefaultMutableTreeNode("<IfSentence>");
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 2) {// if
			i++;
			tr = new DefaultMutableTreeNode("if");
			t.add(tr);
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 17) {// (
			tr = new DefaultMutableTreeNode("(");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}
		tr = new DefaultMutableTreeNode("<IfExpression>");
		if (!isIfExpression(tr)) {
			i = temp;
			return false;
		}
		t.add(tr);
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 18) {// )
			tr = new DefaultMutableTreeNode(")");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}
		tr = new DefaultMutableTreeNode("<Sentence>");
		if (!isSentence(tr)) {
			i = temp;
			return false;
		}
		t.add(tr);
		temp = i;
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 3) {// else
			tr = new DefaultMutableTreeNode("else");
			t.add(tr);
			i++;
			tr = new DefaultMutableTreeNode("<Sentence>");
			if (!isSentence(tr)) {
				i = temp;
			}
			t.add(tr);
		} else {
			//outputError(i-1);
			i = temp;
		}
		f.add(t);
		return true;
	}

	boolean isWhileSentence(DefaultMutableTreeNode f) {
		int temp = i;
		DefaultMutableTreeNode tr, t = new DefaultMutableTreeNode("<WhileSentence>");
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 4) {// while
			i++;
			tr = new DefaultMutableTreeNode("while");
			t.add(tr);
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 17) {// (
			tr = new DefaultMutableTreeNode("(");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}
		tr = new DefaultMutableTreeNode("<IfExpression>");
		if (!isIfExpression(tr)) {
			i = temp;
			return false;
		}
		t.add(tr);
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 18) {// )
			tr = new DefaultMutableTreeNode(")");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}
		tr = new DefaultMutableTreeNode("<Sentence>");
		if (!isSentence(tr)) {
			i = temp;
			return false;
		}
		t.add(tr);
		
		f.add(t);
		return true;
	}

	boolean isCallSentence(DefaultMutableTreeNode f) {
		int temp = i;
		DefaultMutableTreeNode tr, t = new DefaultMutableTreeNode("<CallSentence>");
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 5) {// call
			i++;
			tr = new DefaultMutableTreeNode("call");
			t.add(tr);
		} else {
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 2) {// 标识符
			tr = new DefaultMutableTreeNode("\"" + result.get(i).getContent() + "\"");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}
		f.add(t);
		return true;
	}

	boolean isComplexSentence(DefaultMutableTreeNode f) {
		int temp = i;
		DefaultMutableTreeNode tr, t = new DefaultMutableTreeNode("<ComplexSentence>");
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 6) {// begin
			tr = new DefaultMutableTreeNode("begin");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}

		tr = new DefaultMutableTreeNode("<SentenceList>");
		if (isSentenceList(tr)) {// 语句列表
			t.add(tr);
		} else {
			
			i = temp;
			return false;
		}

		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 1 && result.get(i).getPosition() == 7) {// end
			tr = new DefaultMutableTreeNode("end");
			t.add(tr);
			i++;
		} else {
			//outputError(i-1);
			i = temp;
			return false;
		}
		
		f.add(t);
		return true;
	}

	boolean isExpression(DefaultMutableTreeNode t) {
		int temp = i, count = 1;
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("<term" + String.valueOf(count++) + ">");
		if (!isTerm(tr)) {// 项
			i = temp;
			return false;
		}
		t.add(tr);

		if (result.size() == i) {
			i = temp;
			return false;
		}
		while (result.get(i).getGroupnumber() == 9 || result.get(i).getGroupnumber() == 10) {// +|-
			tr = new DefaultMutableTreeNode(result.get(i).getContent());
			t.add(tr);
			i++;
			tr = new DefaultMutableTreeNode("<term" + String.valueOf(count++) + ">");
			if (!isTerm(tr)) {// 项
				i = temp;
				return false;
			}
			t.add(tr);
		}
		return true;
	}

	boolean isTerm(DefaultMutableTreeNode t) {
		int temp = i, count = 1;
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("<element" + String.valueOf(count++) + ">");
		if (!isElement(tr)) {// 因子
			i = temp;
			return false;
		}
		t.add(tr);

		if (result.size() == i) {
			i = temp;
			return false;
		}
		while (result.get(i).getGroupnumber() == 11 || result.get(i).getGroupnumber() == 12) {// *|/
			tr = new DefaultMutableTreeNode(result.get(i).getContent());
			t.add(tr);
			i++;
			tr = new DefaultMutableTreeNode("<element" + String.valueOf(count++) + ">");
			if (!isElement(tr)) {// 因子
				i = temp;
				return false;
			}
			t.add(tr);
		}
		return true;
	}

	boolean isElement(DefaultMutableTreeNode t) {
		int temp = i;
		DefaultMutableTreeNode tr;
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 2 || result.get(i).getGroupnumber() == 3) {// 整数 | 标识符
			tr = new DefaultMutableTreeNode("\""+result.get(i).getContent()+"\"");
			t.add(tr);
			i++;
			return true;
		} else if (result.get(i).getGroupnumber() == 17) {// (
			tr = new DefaultMutableTreeNode(result.get(i).getContent());
			t.add(tr);
			i++;
			tr = new DefaultMutableTreeNode("<Expression>");
			if (!isExpression(tr)) {// 表达式
				i = temp;
				return false;
			}
			t.add(tr);
			if (result.size() == i) {
				i = temp;
				return false;
			}
			if (result.get(i).getGroupnumber() == 18) {// )
				tr = new DefaultMutableTreeNode(result.get(i).getContent());
				t.add(tr);
				i++;
				return true;
			}
		}
		i = temp;
		return false;
	}

	boolean isIfExpression(DefaultMutableTreeNode t) {
		int temp = i, count = 1;
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("<RelationExpression" + String.valueOf(count++) + ">");
		if (!isRelationExpression(tr)) {// 关系表达式
			i = temp;
			return false;
		}
		t.add(tr);
		while (result.get(i).getGroupnumber() == 1
				&& (result.get(i).getPosition() == 8 || result.get(i).getPosition() == 9)) {
			tr = new DefaultMutableTreeNode(result.get(i).getContent());
			t.add(tr);
			i++;
			tr = new DefaultMutableTreeNode("<RelationExpression" + String.valueOf(count++) + ">");
			if (!isRelationExpression(tr)) {// 关系表达式
				i = temp;
				return false;
			}
			t.add(tr);
		}
		return true;
	}

	boolean isRelationExpression(DefaultMutableTreeNode t) {
		int temp = i;
		DefaultMutableTreeNode tr = new DefaultMutableTreeNode("<Expression>");
		if (!isExpression(tr)) {// 表达式
			i = temp;
			return false;
		}
		t.add(tr);

		tr = new DefaultMutableTreeNode("<RelationOperator>");
		if (!isRelationOperator(tr)) {// 关系运算符
			i = temp;
			return false;
		}
		t.add(tr);

		tr = new DefaultMutableTreeNode("<Expression>");
		if (!isExpression(tr)) {// 表达式
			i = temp;
			return false;
		}
		t.add(tr);

		return true;
	}

	boolean isRelationOperator(DefaultMutableTreeNode t) {
		int temp = i;
		if (result.size() == i) {
			i = temp;
			return false;
		}
		if (result.get(i).getGroupnumber() == 4 || result.get(i).getGroupnumber() == 5
				|| result.get(i).getGroupnumber() == 6 || result.get(i).getGroupnumber() == 7
				|| result.get(i).getGroupnumber() == 13 || result.get(i).getGroupnumber() == 14) {
			/* <= | >= | <> | < | > | == */
			DefaultMutableTreeNode tr = new DefaultMutableTreeNode(result.get(i).getContent());
			t.add(tr);
			i++;
			return true;
		}
		//outputError(i-1);
		i = temp;
		return false;
	}

	public Grammar(Vector<Res> result) {
		this.result = result;
		i = 0;
		root = new DefaultMutableTreeNode("GrammarTree");
		flag = isProgram();
		System.out.println(flag);
	}

	public boolean getFlag() {
		return flag;
	}

	public DefaultMutableTreeNode getRoot() {
		return root;
	}
	public String getError() {
		return error;
	}
}
