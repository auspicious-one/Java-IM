package Image;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.JScrollPane;
import java.awt.Color;

public class Bg extends JFrame {
	public Bg() {
		getContentPane().setBackground(Color.WHITE);
		Icon icon = new ImageIcon("");
		JScrollPane js=new JScrollPane();
		for (int i = 0; i < 100; i++) {
			JTree tree = new JTree();
			tree.setBounds(0, 0, 384, 361);
			tree.setShowsRootHandles(true);
			DefaultMutableTreeNode n = new DefaultMutableTreeNode("ºÃÓÑÁÐ±í");
			DefaultTreeModel m = new DefaultTreeModel(n);
			DefaultMutableTreeNode node_1;
			node_1 = new DefaultMutableTreeNode("colors" + i);
			node_1.add(new DefaultMutableTreeNode("blue"));
			node_1.add(new DefaultMutableTreeNode("violet"));
			node_1.add(new DefaultMutableTreeNode("red"));
			node_1.add(new DefaultMutableTreeNode("yellow"));
			n.add(node_1);
			tree.setModel(m);
			js.setViewportView(tree);
		}
		getContentPane().setLayout(null);
		getContentPane().add(js);
		js.setBounds(0, 0, 384, 361);
		this.setVisible(true);
		this.setSize(400, 400);
	}

	public static void main(String[] args) {
		new Bg();
	}
}
