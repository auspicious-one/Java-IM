package Server;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.sf.json.JSONObject;
import vo.User;

public class Server extends JFrame implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Socket socket = null;
	private ServerSocket serverSocket = null;
	private CopyOnWriteArrayList<ChatThread> clients = new CopyOnWriteArrayList<ChatThread>();
	private CopyOnWriteArrayList<User> userList = new CopyOnWriteArrayList<User>();
	private boolean canRun = true;

	private JPanel plUser = new JPanel(new BorderLayout());
	private JLabel lbUser = new JLabel("在线用户列表:");
	private JPanel chatUser = new JPanel(new BorderLayout());
	private JLabel chatinfo = new JLabel("聊天记录:");
	private static List lstUser = new List();
	private static List lstInfo=new List();
	private JPanel plUser_msg = new JPanel(new GridLayout(1, 2));

	public Server() throws Exception {
		this.setTitle("服务器端已开启!");
		plUser.add(lbUser, BorderLayout.NORTH);
		plUser.add(lstUser, BorderLayout.CENTER);
		chatUser.add(chatinfo,BorderLayout.NORTH);
		chatUser.add(lstInfo,BorderLayout.CENTER);
		lstUser.removeAll();
		lstUser.select(0);
		plUser_msg.add(plUser);
		plUser_msg.add(chatUser);
		this.add(plUser_msg, BorderLayout.CENTER);
		this.setSize(300, 300);
		//this.setVisible(true);
		//创建服务器端口
		serverSocket = new ServerSocket(9999);
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			while (canRun) {
				socket = serverSocket.accept();
				ChatThread ct;
				try {
					ct = new ChatThread(socket, this);
					ct.start();
					//JOptionPane.showMessageDialog(null, "有客户端连接到服务器!");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException ex) {
			canRun = false;
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	//保存连接的线程
	public CopyOnWriteArrayList<ChatThread> getClients() {
		return clients;
	}
	//保存在线的用户
	public CopyOnWriteArrayList<User> getUserList() {
		return userList;
	}
	
	public static void GetData(JSONObject json){
		net.sf.json.JSONArray array = json.getJSONArray("list");
		lstUser.removeAll();
		for (int i = 0; i < array.size(); i++) {
			JSONObject info = array.getJSONObject(i);
			String tel = info.getString("tel");
			String type = info.getString("type");
			lstUser.add("用户ID:" + type +"\t"+ ",帐户:" + tel);		
		}
	}
	public static void GetInfo(JSONObject json){
		if(json.getString("type").equals("message")){
			String from=json.getString("from_name");
			String to=json.getString("to");
			String info=json.getString("info");
			lstInfo.add(from+"-->"+to+"："+info);
		}
	}
}
