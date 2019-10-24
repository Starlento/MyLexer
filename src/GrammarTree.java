

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.Vector;

import javax.swing.*;

import GrammarPackage.Grammar;
import LexPackage.LexAnalysis;
import LexPackage.Res;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;

public class GrammarTree extends JFrame implements MouseListener {
	private static final long serialVersionUID = 1L;
	Vector<Res> result = new Vector<Res>();
	DefaultMutableTreeNode root;
	JPanel left = new JPanel();
	JPanel center = new JPanel();
	JPanel right = new JPanel();
	JPanel left_2 = new JPanel();
	JButton b1 = new JButton("file");
	JButton b2 = new JButton("lex");
	JButton b3 = new JButton("grammar");
	String filepath = null;

	public void init() throws IOException {
		layoutInit();
		showSource();
		setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

	}

	public void layoutInit() {
		setLayout(new BorderLayout());
		left.setPreferredSize(new Dimension(400, 600));
		center.setPreferredSize(new Dimension(400, 600));
		right.setPreferredSize(new Dimension(400, 600));

		left.setLayout(new BorderLayout());
		right.setLayout(new BorderLayout());
		center.setLayout(new BorderLayout());
		JPanel bt = new JPanel();
		bt.setPreferredSize(new Dimension(400, 50));
		b1.addMouseListener(this);
		b2.addMouseListener(this);
		b3.addMouseListener(this);
		bt.add(b1);
		bt.add(b2);
		bt.add(b3);
		left_2.setPreferredSize(new Dimension(400, 550));

		left.add(bt, BorderLayout.NORTH);
		left.add(left_2, BorderLayout.CENTER);

		add(left, BorderLayout.WEST);
		add(center, BorderLayout.CENTER);
		add(right, BorderLayout.EAST);
	}

	public void showSource() throws IOException {
		FileReader fr;
		if (filepath == null) {
			fr = new FileReader(new File(System.getProperty("user.dir")+"\\src\\source.txt"));
		} else {
			fr = new FileReader(filepath);
		}
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		String end = "";
		while ((str != null)) {
			end = end + str + "\n";
			str = br.readLine();
		}
		JTextArea sourceoutput = new JTextArea(end);
		left_2.removeAll();
		sourceoutput.setLineWrap(true); // 激活自动换行功能
		sourceoutput.setWrapStyleWord(true); // 激活断行不断字功能
		JScrollPane st = new JScrollPane(sourceoutput);
		st.setPreferredSize(new Dimension(400, 550));
		left_2.add(st);
		setVisible(false);
		setVisible(true);
		br.close();
	}

	public void doLex() throws IOException {
		result = new Vector<Res>();
		FileReader fr;
		if (filepath == null) {
			fr = new FileReader(new File(System.getProperty("user.dir")+"\\src\\source.txt"));
		}
		else {
			fr = new FileReader(filepath);
		}
		LexAnalysis lexer = new LexAnalysis(result, fr);
		lexer.Print(result);
	}

	public void show2D() throws IOException {
		right.removeAll();
		FileReader fr = new FileReader(new File(System.getProperty("user.dir")+"\\src\\text.txt"));
		BufferedReader br = new BufferedReader(fr);
		String str = br.readLine();
		String end = "";
		while ((str != null)) {
			end = end + str + "\n";
			str = br.readLine();
		}
		JTextArea sourceoutput = new JTextArea(end);
		sourceoutput.setLineWrap(true); // 激活自动换行功能
		sourceoutput.setWrapStyleWord(true); // 激活断行不断字功能
		JScrollPane st = new JScrollPane(sourceoutput);
		st.setPreferredSize(new Dimension(400, 600));
		right.add(st);
		setVisible(false);
		setVisible(true);
		br.close();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(b1)) {
			JFileChooser chooser = new JFileChooser(); // 设置选择器
			chooser.setMultiSelectionEnabled(true); // 设为多选
			chooser.setDialogTitle("请选择生成语法树的文本文件");
			FileNameExtensionFilter filter = new FileNameExtensionFilter("文本文件(*.txt;)", "txt");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(b1); // 是否打开文件选择框
			if (returnVal == JFileChooser.APPROVE_OPTION) { // 如果符合文件类型
				filepath = chooser.getSelectedFile().getAbsolutePath(); // 获取绝对路径
			}
			if (filepath != null) {
				try {
					center.removeAll();
					right.removeAll();
					showSource();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				setVisible(false);
				setVisible(true);
			}
		} else if (e.getSource().equals(b2)) {
			try {
				doLex();
				show2D();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			setVisible(false);
			setVisible(true);
		} else if (e.getSource().equals(b3)) {
			Grammar g = new Grammar(result);
			center.removeAll();
			if (g.getFlag()) {
				JTree tree=new JTree(g.getRoot());
				//tree.expandRow(1);
				JScrollPane temp = new JScrollPane(tree);
				temp.setPreferredSize(new Dimension());
				center.add(temp);
			}
			else {
				JTextArea sourceoutput = new JTextArea("GG my friend"+"\n"+g.getError());
				JScrollPane st = new JScrollPane(sourceoutput);
				st.setPreferredSize(new Dimension(400, 600));
				center.add(st);			
			}
			setVisible(false);
			setVisible(true);
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
