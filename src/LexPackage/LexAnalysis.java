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
		Vector<Lex> table = new Vector<Lex>();// ��ʷ�
		List<Ans> answer = new ArrayList<Ans>();// ����ʵ�Ǹ��м������
		Init(table);// ��ʼ���ʷ����Ѵʷ�������
		String source = Deal(table, answer,fr);// ͨ���ʷ�ƥ��source�ļ��ĵ��ʣ��浽�м������ٶ���ʼλ�ý�������
		Output(answer, result, source);// �Ѵ𰸿ٳ���
	}

	public void Init(Vector<Lex> table) throws IOException {
		FileReader fr = new FileReader(new File(System.getProperty("user.dir")+"\\src\\lex.txt"));
		BufferedReader br = new BufferedReader(fr);
		// �򿪴ʷ��ļ�
		String line = "";
		while ((line = br.readLine()) != null) {
			String[] arrs = null;
			arrs = line.split(" ");
			// ���ո񻮷֣�����Ϊ id�����ݣ�����
			int b = -1;
			try {
				b = Integer.valueOf(arrs[1]).intValue();
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// ��idת��Ϊint����
			table.add(new Lex(arrs[0], b, arrs[2]));
			// ���ȥ
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
		// ���ļ�����String��
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

			} // ����ע�ͣ�ʹ�ö���ģʽ
			else if (temp.getId() == 10002) {
				Matcher m = Pattern.compile(temp.getFormat(), Pattern.DOTALL).matcher(source);
				while (m.find()) {
					Point a = searchPos(pos, m.start());
					answer.add(new Ans((int) a.getX(), m.start() - (int) a.getY(), m.start(), m.end() - m.start(),
							m.group(), temp.getId()));
				}

			} // ����ע�ͣ�ʹ�õ���ģʽ
			else {
				Matcher m = Pattern.compile(temp.getFormat(), Pattern.CASE_INSENSITIVE).matcher(source);
				while (m.find()) {
					Point a = searchPos(pos, m.start());
					answer.add(new Ans((int) a.getX(), m.start() - (int) a.getY(), m.start(), m.end() - m.start(),
							m.group(), temp.getId()));
				}
			}
			// �����ִ�Сд
		} // ��ƥ�䵽��ȫ������answer����ȥ
		Collections.sort(answer);
		// �Ÿ���Ans��������comparable�ӿڣ�
		return source;
		// Ҫ����Ϊ��֪��error�����ݣ��ҲŲ��������������
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
		} // ���
		p.close();
	}

	public void Output(List<Ans> answer, Vector<Res> result, String source) throws IOException {
		int pos = 0;
		int count1 = 0;// �����м�����ʶ��
		int count2 = 0;// �����м�������
		for (int i = 0; i < answer.size(); i++) {// pos��startһ�������һ���𰸣�С��֤�������Ƕ���error�����֤�����ƥ��Ķ��������ȼ����ߵ��������
			if (pos == answer.get(i).getStart()) {
				pos += answer.get(i).getLength();
				if (answer.get(i).getId() >= 10000 && answer.get(i).getId() <= 10002)// �Ǹ�û�õĶ�����
					;
				else if (answer.get(i).getId() >= 19 && answer.get(i).getId() <= 28)// �Ǹ���������
					result.add(new Res(answer.get(i), 1, answer.get(i).getId() - 19));
				else if (answer.get(i).getId() >= 4 && answer.get(i).getId() <= 18 || answer.get(i).getId() == 29)// �Ǹ���������
					result.add(new Res(answer.get(i), answer.get(i).getId(), 0));
				else if (answer.get(i).getId() == 2)// �Ǹ���ʶ����
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

				} else if (answer.get(i).getId() == 3)// �Ǹ�������
					result.add(new Res(answer.get(i), 3, count2++));
			} // �����Ǹ�ɶ���� ���result����
			else if (pos > answer.get(i).getStart())
				;// �����ȼ��ߵĵ�ƥ����ˣ���һλ
			else {
				int x = (answer.get(i).getY() - answer.get(i - 1).getLength()) > 0
						? (answer.get(i).getY() - answer.get(i - 1).getLength())
						: (answer.get(i - 1).getY() + answer.get(i - 1).getLength());
				Ans temp = new Ans(answer.get(i - 1).getX(), x, -1, -1, source.substring(pos, answer.get(i).getStart()),
						-1);
				result.add(new Res(temp, -1, -1));
				pos = answer.get(i).getStart();
				i--;
			} // ����error�˰�
		}
	}
}
