package Register;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import Main.BgWindow;
import net.sf.json.JSONObject;

import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class Register{
	private JTextField qianming;
	private JPasswordField password;
	private JTextField name;
	private ButtonGroup group;
	private BufferedWriter bw;
	private JLabel account;
	String sex = "";

	public Register(String line, BufferedWriter bw) {
		this.bw = bw;
		JFrame jframe = new JFrame();
		BgWindow.WidnowBackground(jframe);
		jframe.getContentPane().setLayout(null);

		JLabel label = new JLabel("\u60A8\u7684\u5E10\u53F7\uFF1A");
		label.setBounds(79, 44, 98, 15);
		jframe.getContentPane().add(label);

		JLabel label_1 = new JLabel("\u8BF7\u8F93\u5165\u5BC6\u7801\uFF1A");
		label_1.setBounds(79, 94, 98, 15);
		jframe.getContentPane().add(label_1);

		JLabel label_2 = new JLabel("\u8BF7\u8F93\u5165\u7B7E\u540D\uFF1A");
		label_2.setBounds(79, 144, 98, 15);
		jframe.getContentPane().add(label_2);

		group = new ButtonGroup();
		JRadioButton radioButton = new JRadioButton("\u7537");
		radioButton.setBounds(203, 240, 55, 23);
		radioButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sex = "男";
			}
		});

		JRadioButton radioButton_1 = new JRadioButton("\u5973");
		radioButton_1.setBounds(257, 240, 55, 23);
		radioButton_1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sex = "女";
			}
		});

		jframe.getContentPane().add(radioButton);
		jframe.getContentPane().add(radioButton_1);
		group.add(radioButton_1);
		group.add(radioButton);
		JButton button = new JButton("注册");
		button.setBounds(79, 286, 214, 31);
		jframe.getContentPane().add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String ps = password.getText();
				String qm = qianming.getText();
				String nm = name.getText();
				String ac=account.getText();
				if (!sex.equals("") && !ps.equals("") && !qm.equals("") && !sex.equals("")
						|| !nm.equals("")) {
					try {
						register(ac, ps, qm, sex, nm);
					} catch (IOException e1) {
					}
				} else {
					JOptionPane.showMessageDialog(null, "请正确填写信息!");
				}
			}
		});

		qianming = new JTextField();
		qianming.setBounds(203, 141, 90, 21);
		jframe.getContentPane().add(qianming);
		qianming.setColumns(10);

		password = new JPasswordField();
		password.setEchoChar('*');
		password.setBounds(203, 91, 90, 21);
		jframe.getContentPane().add(password);

		JLabel lblTips = new JLabel(
				"Tips:\u73B0\u5728\u6CE8\u518C\u6709\u5E0C\u671B\u9886\u53D6\u7BA1\u7406\u5458\u8D44\u683C\u54E6!\r\n\u8BF7\u7262\u8BB0\u60A8\u7684\u8D26\u53F7");
		lblTips.setForeground(new Color(30, 144, 255));
		lblTips.setBounds(42, 327, 332, 23);
		jframe.setTitle("注册新用户!");
		jframe.getContentPane().add(lblTips);
		JLabel label_3 = new JLabel("\u8BF7\u9009\u62E9\u6027\u522B\uFF1A");
		label_3.setBounds(79, 244, 98, 15);
		jframe.getContentPane().add(label_3);

		JLabel label_4 = new JLabel("\u65B0\u7528\u6236\u6CE8\u518C");
		label_4.setFont(new Font("宋体", Font.PLAIN, 18));
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		label_4.setBounds(145, 10, 99, 24);
		jframe.getContentPane().add(label_4);

		JLabel lblNewLabel = new JLabel("\u8BF7\u8F93\u5165\u6635\u79F0\uFF1A");
		lblNewLabel.setBounds(79, 194, 98, 15);
		jframe.getContentPane().add(lblNewLabel);

		name = new JTextField();
		name.setBounds(203, 191, 90, 21);
		jframe.getContentPane().add(name);
		name.setColumns(10);
		
		String acc=String.valueOf((int)(Math.random()*1000000+100000));
		account = new JLabel(acc);
		account.setHorizontalAlignment(SwingConstants.CENTER);
		account.setForeground(Color.RED);
		account.setFont(new Font("宋体", Font.PLAIN, 18));
		account.setBounds(203, 44, 90, 15);
		jframe.getContentPane().add(account);
		jframe.setVisible(true);
		jframe.setSize(400, 413);
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

	public void register(String ac, String ps, String qm, String sex, String nm) throws IOException {
		JSONObject json = new JSONObject();
		json.put("type", "register");
		json.put("tel", ac);
		json.put("password", ps);
		json.put("qianming", qm);
		json.put("name", nm);
		json.put("sex",sex);
		bw.write(json + "\n");
		bw.flush();
	}

	public static void listener(String line) throws IOException {
		JSONObject json = JSONObject.fromObject(line);
		String type = json.getString("type");
		if (type.equals("register_info")) {
			if (json.getBoolean("canRegister") == true) {
				JOptionPane.showMessageDialog(null, "恭喜你注册成功!登录去-->");
			} else {
				JOptionPane.showMessageDialog(null, "帐户已存在，请重新输入!");
			}
		}
	}
}
