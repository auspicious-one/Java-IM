package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import Dao.LoginDao;
import Dao.SetUser;
import vo.*;

public class ChatThread extends Thread {
	private Socket socket = null;
	private User user = new User(); // ʵ����һ��
	private Server server;
	private boolean canRun = true;
	private BufferedReader br = null;
	private BufferedWriter bw = null;
	int i = 0;
	boolean isRun;

	public ChatThread(Socket socket, Server server) throws Exception {
		this.socket = socket;
		this.server = server;
		br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
	}

	public void run() {
		try {
			while (canRun) {
				JSONObject json = JSONObject.fromObject(br.readLine().toString());
				String type = json.getString("type");
				if (type.equals("login")) {
					this.Login(json);
				} else if (type.equals("message")) {
					this.Message(json);
					Server.GetInfo(json);
				} else if (type.equals("register")) {
					this.Register(json);
				} else if (type.equals("search")) {
					this.Search(json);
				} else if (type.equals("file")) {
					ReceiveFile.receiveFile(socket, json.getString("filename"));
				}else if(type.equals("addfriend")){
					this.Message(json);
					System.out.println("��������:"+json);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception ex) {
			this.LiveOut(); // �ͻ������ӶϿ���ɾ�����û�
		}
	}

	// �����ѯ����Ϣ,
	public void Search(JSONObject json) throws Exception {
		String info = json.getString("info");
		String from = json.getString("from_num");
		ArrayList all = (ArrayList) new LoginDao().Search(info);
		if (all != null) {
			JSONObject result = new JSONObject();
			JSONArray array = new JSONArray();
			JSONObject rinfo = new JSONObject();
			for (int i = 0; i < all.size(); i++) {
				Login login = (vo.Login) all.get(i);
				if (!login.getTel().equals(from)) {			//�жϲ�ѯ�����û����ǵ�ǰ�û�
					rinfo.put("type", "login_info");
					rinfo.put("userId", login.getUserid());
					rinfo.put("account", login.getAccount());
					rinfo.put("password", login.getPassword());
					rinfo.put("sex", login.getSex());
					rinfo.put("qianming", login.getQianming());
					rinfo.put("tel", login.getTel());
					rinfo.put("admin", login.getAdmin());
					array.add(rinfo);
				}
			}
			result.put("type", "result_info");
			result.put("to", from);
			result.put("result", array);
			SendMessage(from, result.toString());
		}
	}

	// ��Ϣ�����࣬�����߱��������ݿ�
	public void Message(JSONObject json) throws IOException {
		boolean canSend = false;
		String to = json.getString("to");
		for (ChatThread ct : server.getClients()) {
			if (ct.user.getTel().equals(to) || to.equals("All")) { // �ж��û�Ҫ������Ϣ�Ķ���
				ct.bw.write(json.toString() + "\n");
				ct.bw.flush();
				canSend = true;
			}
		}
		if (canSend == false) {
			String from_num = json.getString("from_num");
			String from_name = json.getString("from_name");
			String info = json.getString("info");
			LoginDao.Retain(from_name, from_num, to, info);
		}
	}

	// �����û���¼
	public void Register(JSONObject json) throws Exception {
		String tel = json.getString("tel");
		String password = json.getString("password");
		String qianming = json.getString("qianming");
		String name = json.getString("name");
		String sex = json.getString("sex");
		LoginDao res = new LoginDao();
		Register rs = res.addUser(tel, password, qianming, name, sex);
		JSONObject json1 = new JSONObject();
		if (rs.isCanRegister()) {
			json1.put("type", "register_info");
			json1.put("canRegister", true);
		} else {
			json1.put("type", "register_info");
			json1.put("canRegister", false);
		}
		Register_out(qianming, json1);
	}

	// �ͻ��˵�¼
	public void handleLogin(int j, String tel, String name, String qianming, String sex, int k) throws Exception {
		User user = SetUser.setType(j, tel, name, qianming, sex, k);
		this.user = user;
		if (server.getClients().size() == 0) {
			server.getClients().add(this);
			server.getUserList().add(this.user);
			this.SendAll();
			server.setTitle("��ǰ�����û�:" + server.getClients().size() + "��");
			isRun = true;
		} else {
			for (ChatThread ct : server.getClients()) {
				if ((ct.user.getType() != j)) { // �жϵ�ǰ�û������������
					server.getClients().add(this);
					server.getUserList().add(this.user);
					this.SendAll();
					server.setTitle("��ǰ�����û�:" + server.getClients().size() + "��");
					isRun = true;
				} else {
					isRun = false;
				}
			}
		}

	}

	// �ͻ����뿪
	public void LiveOut() {
		server.getClients().remove(this); // Ӧ����ɾ���������ӡ���Ȼѭ�����һᱨ�쳣��
		if (this.user.getTel() != null) {
			JSONObject json = new JSONObject();
			json.put("type", "login_out");
			json.put("name", this.user.getAccount());
			json.put("tel", this.user.getTel());
			Friend_outLine(this.user.getType(), json);
		}
		server.getUserList().remove(this.user);
		try {
			this.SendAll();
			canRun = false;
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("���û��˳�����ǰ�����û�:" + server.getClients().size());
		server.setTitle("��ǰ�����û�:" + server.getClients().size() + "��");
	}

	// ���Ϳͻ�����Ϣ
	public void SendMessage(String to, String info) throws Exception {
		for (ChatThread ct : server.getClients()) {
			if (ct.user.getTel().equals(to) || to.equals("All")) { // �ж��û�Ҫ������Ϣ�Ķ���
				ct.bw.write(info + "\n");
				ct.bw.flush();
			}
		}
	}

	// ��ע���û�������Ϣ
	public void SendRegisterMessage(String to, String info) throws Exception {
		for (ChatThread ct : server.getClients()) {
			if (ct.user.getQianming().equals(to)) { // �ж��û�Ҫ������Ϣ�Ķ���
				ct.bw.write(info + "\n");
				ct.bw.flush();
			}
		}
	}

	// �������û�����������Ϣ����ʱȡ��������
	public void SendAll() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		JSONObject all = new JSONObject();
		try {
			for (ChatThread ct : server.getClients()) { // ѭ����JSON���������
				all.put("type", ct.user.getType());
				all.put("tel", ct.user.getTel());
				all.put("account", ct.user.getAccount());
				array.add(all);
			}
			json.put("list", array);
			json.put("type", "list");
			Server.GetData(json); // �����������ݵ�������
			// SendMessage("All", json.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// ��¼�ɹ������ظ�����Ϣ�������ҵĺ�����Ϣ���������ߺ�����Ϣ,�����ҵ�������Ϣ
	public void Login(JSONObject json) throws Exception {
		String tel = json.getString("tel");
		String password = json.getString("password");
		LoginDao logindao = new LoginDao();
		Login l = logindao.checkLogin(tel, password);
		JSONObject login_info = new JSONObject();
		if (l != null) {
			// ��¼�ɹ�,���ظ�����Ϣ
			login_info.put("type", "login_info");
			login_info.put("userId", l.getUserid());
			login_info.put("account", l.getAccount());
			login_info.put("password", l.getPassword());
			login_info.put("sex", l.getSex());
			login_info.put("qianming", l.getQianming());
			login_info.put("tel", l.getTel());
			login_info.put("admin", l.getAdmin());
			this.handleLogin(l.getUserid(), l.getTel(), l.getAccount(), l.getQianming(), l.getSex(), l.getAdmin());
			// �жϵ�ǰ��¼�û��Ƿ������߳���
			if (isRun == true) {
				this.SendMessage(l.getTel(), login_info.toString());
				// �����ҵĺ�����Ϣ
				ArrayList f = logindao.getAll(l.getUserid());
				JSONObject root = new JSONObject();
				JSONArray array = new JSONArray();
				JSONObject jsonid = new JSONObject();
				for (int i = 0; i < f.size(); i++) {
					Friend in = (Friend) f.get(i);
					jsonid.put("userId", in.getUserid());
					jsonid.put("name", in.getAccount());
					jsonid.put("sex", in.getSex());
					jsonid.put("qianming", in.getQianming());
					jsonid.put("tel", in.getTel());
					// ��ѯ���ߺ����е����������������͸��ͻ��ˣ��趨1���ߣ�0����
					boolean inLine = false; // �趨���ѳ�ʼ״̬Ϊ����
					for (ChatThread ct : server.getClients()) {
						if (ct.user.getType() == in.getUserid()) {
							Friend_onLine(ct.user.getTel(), l.getUserid(), l.getTel(), l.getAccount(), l.getQianming(),
									l.getSex()); // ���û�������ʾ��Ϣ����ʾ��ǰ�û�����
							inLine = true;
						}
					}
					if (inLine == true) {
						jsonid.put("friend_online", "1");
					} else {
						jsonid.put("friend_online", "0");
					}
					array.add(jsonid);
				}
				root.put("type", "all_friends");
				root.put("all", array);
				this.SendMessage(l.getTel(), root.toString());

				SendOutMessage(l.getTel());
			} else {
				login_info.put("type", "login_info");
				login_info.put("userId", 0);
				login_info.put("tel", tel);
				Login_out(-1, tel, login_info);
			}
		} else {
			login_info.put("type", "login_info");
			login_info.put("userId", -1);
			login_info.put("tel", tel);
			Login_out(-1, tel, login_info);
		}
	}

	// ���Լ��������ݿ��б�������Ϣ
	public void SendOutMessage(String num) throws Exception {
		LoginDao login = new LoginDao();
		ArrayList out = login.outLineInfo(num);
		if (out != null) {
			for (int i = 0; i < out.size(); i++) {
				outLineInfo info = (outLineInfo) out.get(i);
				JSONObject json = new JSONObject();
				json.put("type", "message");
				json.put("from_num", info.getFrom_num());
				json.put("from_name", info.getFrom_name());
				json.put("to", info.getTo_num());
				json.put("info", info.getInfo());
				SendMessage(info.getTo_num(), json.toString());
			}
		}
	}

	// ��Ϊ ��¼ʧ�ܵķ���
	public void Login_out(int id, String tel, JSONObject json) throws Exception {
		User user = SetUser.setType(id, tel, "", "", "", 0);
		this.user = user;
		server.getClients().add(this);
		server.getUserList().add(this.user);
		SendMessage(tel, json.toString());
		server.getClients().remove(this);
		server.getUserList().remove(this.user);
	}

	// ����ע����Ϣ
	public void Register_out(String qianming, JSONObject json) throws Exception {
		User user = SetUser.setType(0, "", "", qianming, "", 0);
		this.user = user;
		server.getClients().add(this);
		server.getUserList().add(this.user);
		SendRegisterMessage(qianming, json.toString());
		server.getClients().remove(this);
		server.getUserList().remove(this.user);
	}

	// ���������û������Ѿ�����
	public void Friend_onLine(String tel, int id, String Itel, String name, String qianming, String sex)
			throws Exception {
		JSONObject json = new JSONObject();
		json.put("type", "notice_login");
		json.put("userId", id);
		json.put("name", name);
		json.put("tel", Itel);
		json.put("qianming", qianming);
		json.put("sex", sex);
		SendMessage(tel, json.toString());
	}

	// �������ߺ��ѣ���������
	public void Friend_outLine(int id, JSONObject json) {
		LoginDao logindao = new LoginDao();
		try {
			ArrayList f = logindao.getAll(id);
			if (f != null) {
				for (int i = 0; i < f.size(); i++) {
					Friend in = (Friend) f.get(i);
					for (ChatThread ct : server.getClients()) {
						if (ct.user.getType() == in.getUserid()) {
							SendMessage(ct.user.getTel(), json.toString());
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
