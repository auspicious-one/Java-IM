package Image;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class lokkand {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.LookAndFeelInfo[] infos=UIManager.getInstalledLookAndFeels();
		for(UIManager.LookAndFeelInfo info:infos){
			UIManager.setLookAndFeel(info.getClassName());
			JOptionPane.showInputDialog(info.getName()+"·ç¸ñ");
			System.out.println(info.getName()+"£º "+info.getClassName());
		}
	}

}
