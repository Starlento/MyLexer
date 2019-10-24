package LexPackage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.*;
import java.awt.Point;

public class LexAnalysis {
	public LexAnalysis(Vector<Res> result,FileReader fr) throws IOException {
		Vector<Lex> table = new Vector<Lex>();// 存词法
		List<Ans> answer = new ArrayList<Ans>();// 这其实是个中间过程啦
		Init(table);// 初始化词法（把词法读进来
		String source = Deal(table, answer,fr);// 通过词法匹配source文件的单词，存到中间变量里，再对起始位置进行排序
		Output(answer, result, source);// 把答案抠出来
	}

	public void Init(Vector<Lex> table) throws IOException {
		FileReader fr = new FileReader(new File(System.getProperty("user.dir")+"\\src\\lex.txt"));
		BufferedReader br = new BufferedReader(fr);
		// 打开词法文件
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] arrs = null;
			arrs = line.split(" ");
			// 按空格划分，依次为 id，内容，名称
			int b = -1;
			try {
				b = Integer.valueOf(arrs[1]).intValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// 把id转换为int类型
			table.add(new Lex(arrs[0], b, arrs[2]));
			// 存进去
		}
		br.close();
		fr.close();
	}

	public String Deal(Vector<Lex> table, List<Ans> answer,FileReader fr) throws IOException {
		BufferedReader br = new BufferedReader(fr);
		String source = new String();
		String line = "";
		while ((line = br.readLine()) != null) {
			source += line;
			source += "\n";
		}
		br.close();
		fr.close();
		// 把文件读到String里
		Vector<Integer> pos = new Vector<Integer>();
		pos.add(-1);
		getPos(pos, source);
		for (int i = 0; i < table.size(); i++) {
			Lex temp = table.elementAt(i);
			if (temp.getId() == 10001) {
				Matcher m = Pattern.compile(temp.getFormat(), Pattern.MULTILINE).matcher(source);
				while (m.find()) {
					Point a = searchPos(pos, m.start());
					answer.add(new Ans((int) a.getX(), m.start() - (int) a.getY(), m.start(), m.end() - m.start(),
							m.group(), temp.getId()));
				}

			} // 单行注释，使用多行模式
			else if (temp.getId() == 10002) {
				Matcher m = Pattern.compile(temp.getFormat(), Pattern.DOTALL).matcher(source);
				while (m.find()) {
					Point a = searchPos(pos, m.start());
					answer.add(new Ans((int) a.getX(), m.start() - (int) a.getY(), m.start(), m.end() - m.start(),
							m.group(), temp.getId()));
				}

			} // 多行注释，使用单行模式
			else {
				Matcher m = Pattern.compile(temp.getFormat(), Pattern.CASE_INSENSITIVE).matcher(source);
				while (m.find()) {
					Point a = searchPos(pos, m.start());
					answer.add(new Ans((int) a.getX(), m.start() - (int) a.getY(), m.start(), m.end() - m.start(),
							m.group(), temp.getId()));
				}
			}
			// 不区分大小写
		} // 把匹配到的全部塞到answer里面去
		Collections.sort(answer);
		// 排个序（Ans类重载了comparable接口）
		return source;
		// 要不是为了知道error的内容，我才不返回这个玩意呢
	}

	public void getPos(Vector<Integer> pos, String source) {
		for (int i = 0; i < source.length(); i++) {
			if (source.charAt(i) == '\n')
				pos.add(i);
		}
	}

	public Point searchPos(Vector<Integer> pos, int t) {
		for (int i = 1; i < pos.size(); i++) {
			if (pos.get(i) > t)
				return new Point(i, pos.get(i - 1).intValue());
		}
		return new Point(-1, -1);
	}

	public int SearchIdentifer(Vector<Res> result, String a) {
		for (int i = 0; i < result.size(); i++) {
			if (a.equals(result.get(i).getContent()))
				return i;
		}
		return -1;
	}

	public void Print(Vector<Res> result) throws FileNotFoundException, IOException {
		File f=new File(System.getProperty("user.dir")+"\\src\\text.txt");
		if(f.exists()) {
			f.delete();
			f.createNewFile();
		}
		FileOutputStream fs = new FileOutputStream(f);
		PrintStream p = new PrintStream(fs);

		for (int i = 0; i < result.size(); i++) {
			result.get(i).printRes(p);
		} // 输出
		p.close();
	}

	public void Output(List<Ans> answer, Vector<Res> result, String source) throws IOException {
		int pos = 0;
		int count1 = 0;// 计数有几个标识符
		int count2 = 0;// 计数有几个数字
		for (int i = 0; i < answer.size(); i++) {// pos比start一样就输出一个答案，小就证明后面那段是error，大就证明这个匹配的东西被优先级更高的输出掉了
			if (pos == answer.get(i).getStart()) {
				pos += answer.get(i).getLength();
				if (answer.get(i).getId() >= 10000 && answer.get(i).getId() <= 10002)// 是个没用的东西呢
					;
				else if (answer.get(i).getId() >= 19 && answer.get(i).getId() <= 28)// 是个保留字呢
					result.add(new Res(answer.get(i), 1, answer.get(i).getId() - 19));
				else if (answer.get(i).getId() >= 4 && answer.get(i).getId() <= 18 || answer.get(i).getId() == 29)// 是个操作符呢
					result.add(new Res(answer.get(i), answer.get(i).getId(), 0));
				else if (answer.get(i).getId() == 2)// 是个标识符呢
				{
					if (answer.get(i).getLength() > 20)
						result.add(new Res(answer.get(i), -1, -1));
					else {
						int judge = SearchIdentifer(result, answer.get(i).getContent());
						if (judge == -1)
							result.add(new Res(answer.get(i), 2, count1++));
						else
							result.add(new Res(answer.get(i), 2, result.get(judge).getPosition()));
					}

				} else if (answer.get(i).getId() == 3)// 是个整数呢
					result.add(new Res(answer.get(i), 3, count2++));
			} // 看看是个啥东西 填进result里面
			else if (pos > answer.get(i).getStart())
				;// 被优先级高的的匹配掉了，下一位
			else {
				int x = (answer.get(i).getY() - answer.get(i - 1).getLength()) > 0
						? (answer.get(i).getY() - answer.get(i - 1).getLength())
						: (answer.get(i - 1).getY() + answer.get(i - 1).getLength());
				Ans temp = new Ans(answer.get(i - 1).getX(), x, -1, -1, source.substring(pos, answer.get(i).getStart()),
						-1);
				result.add(new Res(temp, -1, -1));
				pos = answer.get(i).getStart();
				i--;
			} // 看，error了吧
		}
	}
}
