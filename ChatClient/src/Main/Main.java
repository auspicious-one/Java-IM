package Main;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import Login.LoginView;

public class Main{
	public static void main(String[] args) {
		new LoginView();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
}
