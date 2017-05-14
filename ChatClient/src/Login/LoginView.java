package Login;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import Main.BgWindow;
import Main.InfoImage;
import Register.Register;
import Server.Customer;
import net.sf.json.JSONObject;
import vo.User;

import javax.swing.JPasswordField;
import javax.swing.JCheckBox;
import java.awt.Color;

/**
 * @author 登录界面
 *
 */
public class LoginView implements Runnable {
	private JFrame jframe;
	private static final long serialVersionUID = 1L;
	private JTextField text_account;
	static Socket socket = null;
	private JPasswordField text_password;
	private BufferedReader br;
	private static BufferedWriter bw;
	JSONObject json;
	boolean canRun = false;
	boolean inLine = false;
	private User user = null;
	String line;

	public LoginView() {
		jframe = new JFrame();
		BgWindow.WidnowBackground(jframe);
		jframe.getContentPane().setBackground(Color.WHITE);
		jframe.getContentPane().setLayout(null);

		text_account = new JTextField();
		text_account.setBounds(198, 157, 93, 21);
		jframe.getContentPane().add(text_account);
		text_account.setColumns(10);

		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u5E10\u6237 :");
		lblNewLabel.setBounds(95, 160, 80, 15);
		jframe.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("\u8BF7\u8F93\u5165\u5BC6\u7801 :");
		lblNewLabel_1.setBounds(95, 200, 80, 15);
		jframe.getContentPane().add(lblNewLabel_1);

		JButton btnNewButton = new JButton("登录");
		btnNewButton.setBounds(95, 280, 93, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String account = text_account.getText();
				String password = text_password.getText();
				if (account.equals("") || password.equals("") || account == null || password == null) {
					JOptionPane.showMessageDialog(null, "请正确填写信息!");
					text_account.setText("");
					text_password.setText("");
				} else {
					if (inLine == true) {
						login(account, password);
					} else {
						JOptionPane.showMessageDialog(null, "服务器未连接!不能登录");
					}
				}
			}
		});
		jframe.getContentPane().add(btnNewButton);

		JButton btnNewButton_1 = new JButton("注册");
		btnNewButton_1.setBounds(198, 280, 93, 23);
		jframe.getContentPane().add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (inLine == true) {
					new Register(line, bw);
				} else {
					JOptionPane.showMessageDialog(null, "服务器未连接!不能注册");
				}
			}

		});
		text_password = new JPasswordField();
		text_password.setBounds(198, 197, 93, 21);
		jframe.getContentPane().add(text_password);

		JLabel label = new JLabel(InfoImage.InfoImage());
		label.setBounds(95, 20, 196, 113);
		jframe.getContentPane().add(label);

		final JCheckBox checkBox = new JCheckBox("\u8BB0\u4F4F\u5BC6\u7801");
		checkBox.setSelected(true);
		checkBox.setBackground(Color.WHITE);
		checkBox.setBounds(95, 240, 103, 23);
		jframe.getContentPane().add(checkBox);

		jframe.setVisible(true);
		jframe.setSize(400, 370);
		jframe.setResizable(false);
		jframe.setLocationRelativeTo(jframe.getOwner());
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		new Thread(this).start();
	}

	@Override
	public void run() {
		try {
			socket = new Socket("localhost", 9999);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
			jframe.setTitle("服务器已连接");
			inLine = true;
			loginInfo();
		} catch (UnknownHostException e) {
			JOptionPane.showMessageDialog(null, "服务器未连接!");
			jframe.setTitle("服务器未连接");
			inLine = false;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "服务器未连接!");
			jframe.setTitle("服务器未连接");
			inLine = false;
		}
	}

	public void login(String num, String password) {
		JSONObject json = new JSONObject();
		json.put("type", "login");
		json.put("tel", num);
		json.put("password", password);
		try {
			bw.write(json + "\n");
			bw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loginInfo() {
		line = null;
		StringBuffer sb = null;
		try {
			while ((line = br.readLine()) != null) {
				json = JSONObject.fromObject(line);
				AllStart(line);
				Register.listener(line);
				if (json.getString("type").equals("login_info")) {
					int id = json.getInt("userId");
					if (id > 0) {
						setInfo(json);
						jframe.dispose();
						new Customer(user);
					} else if (id == 0) {
						JOptionPane.showMessageDialog(null, "您的帐户已登录!\n请确认账户安全或者更改密码!");
					} else if (id < 0) {
						JOptionPane.showMessageDialog(null, "登录失败，请重试!\n请检查密码是否正确!");
					}
				}
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "服务器断开，系统即将退出!");
			new LoginView();
			System.exit(0);
			e.printStackTrace();
		}
	}

	// 传递信息Line
	public void AllStart(String line) {
		Customer.recLogin_in(line);
	}

	// 把个人信息保存在user中
	public void setInfo(JSONObject json) {
		user = new User();
		user.setAccount(json.getString("account"));
		user.setPassword(json.getString("password"));
		user.setQianming(json.getString("qianming"));
		user.setSex(json.getString("sex"));
		user.setTel(json.getString("tel"));
		user.setUserid(json.getInt("userId"));
		user.setAdmin(json.getInt("admin"));
	}

	// 静态类，发送聊天信息
	public static void SendMessage(JSONObject json) {
		try {
			bw.write(json + "\n");
			bw.flush();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}

	public static void SendFile(byte[] b,int length) throws IOException {
		DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
		dos.write(b, 0, length);
		dos.flush();
	}
}
