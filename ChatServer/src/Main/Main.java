package Main;

import Server.Server;


public class Main {
	public static void main(String[] args) {
		try {
			@SuppressWarnings("unused")
			Server server=new Server();
			System.out.println("服务器已开启");
		} catch (Exception e) {
			System.out.println("服务器开启失败");
		}
	}

}
