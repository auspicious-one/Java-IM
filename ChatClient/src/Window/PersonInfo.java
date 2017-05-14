package Window;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JPanel;

import Login.LoginView;
import Main.BgWindow;
import Main.InfoImage;
import net.sf.json.JSONObject;
import vo.AllUser;
import vo.Search;
import vo.User;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;

public class PersonInfo implements ActionListener {
	private Icon welcome = null;
	private JLabel account;
	private JLabel name;
	private JLabel qianming;
	private JLabel sex;
	private ArrayList<Search> search_info = null;
	private ArrayList<AllUser> allUserList = null;
	String url = null;
	String num = null;
	User user=null;
	public PersonInfo(ArrayList<Search> search_info, String num, ArrayList<AllUser> allUserList, User user) {
		this.search_info = search_info;
		this.num = num;
		this.allUserList = allUserList;
		this.user=user;
		JFrame jframe = new JFrame();
		BgWindow.WidnowBackground(jframe);
		jframe.getContentPane().setBackground(Color.WHITE);
		jframe.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel(InfoImage.InfoImage());
		lblNewLabel.setBounds(150, 20, 100, 96);
		jframe.getContentPane().add(lblNewLabel);

		account = new JLabel("\u8D26\u53F7\uFF1A");
		account.setFont(new Font("宋体", Font.PLAIN, 16));
		account.setBounds(100, 140, 274, 25);
		jframe.getContentPane().add(account);

		name = new JLabel("\u6635\u79F0\uFF1A");
		name.setFont(new Font("宋体", Font.PLAIN, 16));
		name.setBounds(100, 190, 274, 25);
		jframe.getContentPane().add(name);

		qianming = new JLabel("\u7B7E\u540D\uFF1A");
		qianming.setFont(new Font("宋体", Font.PLAIN, 16));
		qianming.setBounds(100, 240, 274, 25);
		jframe.getContentPane().add(qianming);

		sex = new JLabel("\u6027\u522B\uFF1A");
		sex.setFont(new Font("宋体", Font.PLAIN, 16));
		sex.setBounds(100, 290, 274, 25);
		jframe.getContentPane().add(sex);

		JButton button = new JButton("加为好友");
		button.setBounds(150, 328, 93, 23);
		button.addActionListener(this);
		jframe.getContentPane().add(button);
		jframe.setTitle("用户信息!");
		jframe.setVisible(true);
		jframe.setSize(400, 400);
		jframe.setLocationRelativeTo(jframe.getOwner());
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jframe.setResizable(false);
		result();
	}

	public void result() {
		for (Search s : search_info) {
			if (s.getTel().equals(num)) {
				account.setText("帐户：" + s.getTel());
				name.setText("昵称：" + s.getAccount());
				qianming.setText("签名：" + s.getQianming());
				sex.setText("性别：" + s.getSex());
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		boolean isInList = false;
		for (AllUser user : allUserList) {
			if (user.getTel().equals(account.getText().split("：")[1])) {
				isInList = true;
			}
		}
		if (isInList == true) {
			JOptionPane.showMessageDialog(null, "该用户已在您的好友列表中了!\n请勿重复添加!");
		} else {
			JSONObject json = new JSONObject();
			json.put("type", "addfriend");
			json.put("from_num", user.getTel());
			json.put("from_name",user.getAccount());
			json.put("info","canAdd");
			json.put("to", account.getText().split("：")[1]);
			LoginView.SendMessage(json);
			JOptionPane.showMessageDialog(null, "已向用户:" + account.getText().split("：")[1] + "\n发出好友请求!");
		}
	}
}
