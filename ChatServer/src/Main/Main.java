package Main;

import Server.Server;


public class Main {
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			Server server=new Server();
			System.out.println("�������ѿ���");
		} catch (Exception e) {
			System.out.println("����������ʧ��");
		}
	}

}
