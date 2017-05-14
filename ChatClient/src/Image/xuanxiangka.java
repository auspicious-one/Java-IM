package Image;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JTree;
import javax.swing.JSpinner;

public class xuanxiangka  extends JFrame{
	public xuanxiangka(){
		getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 299, 378);
		getContentPane().add(panel);
		
		JScrollPane sc=new JScrollPane();
		panel.add(sc);
		for(int i=0;i<=100;i++){
			JLabel jl=new JLabel(String.valueOf(i+"²âÊÔ"));
			sc.add(jl);
		}
		this.setVisible(true);
		this.setSize(315, 417);
	}
	public static void main(String[] args) {
		xuanxiangka a=new xuanxiangka();
	}
}
