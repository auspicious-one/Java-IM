package Window;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ImageList.ImageList;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import vo.User;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import Login.LoginView;
import Main.BgWindow;
import Server.Customer;
import java.awt.Color;
import javax.swing.JScrollPane;

public class ChatWindow implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private JLabel chatTitle;

	private JPanel plSend = new JPanel();
	private JTextArea sendMsg = new JTextArea();
	private JButton btnSend = new JButton("发送");
	private JPanel parent_window = new JPanel();
	static String list = null;
	String line = null;
	static User user = null;
	static JList userList;
	static DefaultListModel listModel;
	static int j = 0; // 列表项
	private JScrollPane js;
	private final JButton button = new JButton("发送文件");

	// list 为窗口选中项,line为传过来的数据
	public ChatWindow(String list, String line, User user) {
		JFrame jframe = new JFrame();
		String url = "background/customer_bg.jpg";
		BgWindow.WidnowBackground(jframe);
		this.list = list;
		this.line = line;
		this.user = user;
		chatTitle = new JLabel(list);
		jframe.getContentPane().setLayout(null);
		plSend.setBounds(10, 261, 556, 79);
		plSend.setBackground(Color.WHITE);
		plSend.setLayout(null);
		JScrollPane jsc = new JScrollPane(sendMsg);
		jsc.setLocation(0, 0);
		jsc.setSize(556, 79);
		sendMsg.setBounds(0, 0, 556, 79);
		sendMsg.setBackground(Color.WHITE);
		sendMsg.setForeground(Color.BLACK);
		sendMsg.setLineWrap(true);
		plSend.add(jsc);
		jframe.getContentPane().add(plSend);

		parent_window.setBackground(Color.WHITE);
		parent_window.setBounds(10, 10, 556, 241);
		jframe.getContentPane().add(parent_window);

		btnSend.setBounds(490, 340, 76, 36);
		jframe.getContentPane().add(btnSend);
		btnSend.setForeground(Color.BLACK);
		btnSend.setBackground(Color.WHITE);

		btnSend.addActionListener(this);
		btnSend.setActionCommand("0");
		listModel = new DefaultListModel();
		parent_window.setLayout(null);

		userList = new JList(listModel);
		js = new JScrollPane(userList);
		js.setLocation(0, 0);
		js.setSize(556, 241);
		parent_window.add(js);
		userList.setBounds(0, 0, 556, 241);

		jframe.getContentPane().setBackground(new Color(175, 238, 238));
		button.setBounds(394, 340, 93, 36);
		button.addActionListener(this);
		button.setActionCommand("1");
		jframe.getContentPane().add(button);
		jframe.setBackground(Color.WHITE);
		jframe.setSize(592, 415);
		jframe.setVisible(true);
		jframe.setTitle(list);
		jframe.addWindowListener(this);
		jframe.setLocationRelativeTo(jframe.getOwner());
	}

	public static void recMessage(String line) {
		JSONObject json;
		try {
			json = JSONObject.fromObject(line);
			if (json.getString("type").equals("message")) {
				String from_num = json.getString("from_num");
				String from_name = json.getString("from_name");
				String to = json.getString("to");
				String info = json.getString("info");
				if (list != null && user.getTel() != null) {
					setIcon();
					if (from_num.equals(list.split("-")[0]) && to.equals(user.getTel())) {
						listModel.add(j++, from_name + " :" + info + "\n");
					} else if (to.equals("All")) {
						if (from_num.equals(user.getTel())) {
							listModel.add(j++, "我 :" + info + "\n");
						} else {
							listModel.add(j++, from_name + " :" + info + "\n");
						}
					}
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("0")) {
			if (sendMsg.getText() != null && !sendMsg.getText().equals("")) {
				setIcon();
				JSONObject json = new JSONObject();
				try {
					json.put("type", "message");
					json.put("from_name", user.getAccount());
					json.put("from_num", user.getTel());
					json.put("to", list.split("-")[0]);
					json.put("info", sendMsg.getText());
					LoginView.SendMessage(json);
					if (!list.split("-")[0].equals("All")) {
						listModel.add(j++, "我 :" + sendMsg.getText() + "\n");
					}
					sendMsg.setText("");
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
			}
		} else if (e.getActionCommand().equals("1")) {
			JFileChooser jfc = new JFileChooser();
			jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			jfc.showDialog(new JLabel(), "选择");
			File file = jfc.getSelectedFile();
			if(file!=null){
				if (file.isDirectory()) {
					System.out.println("文件夹位置:" + file.getAbsolutePath());
				} else if (file.isFile()) {
					System.out.println("文件位置:" + file.getAbsolutePath());
				}
				try {
					new SendFile(file.getAbsolutePath(),file.getName());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	public static void info_new(String name, String info) {
		listModel.add(0, name + " :" + info + "\n");
	}

	public static void setIcon() {
		Icon icon1 = new ImageIcon("new_info/icon1.jpg");
		Icon icon2 = new ImageIcon("new_info/icon2.jpg");
		Icon icon3 = new ImageIcon("new_info/icon3.jpg");
		Icon icon4 = new ImageIcon("new_info/icon4.jpg");
		Icon icon5 = new ImageIcon("new_info/icon5.jpg");
		Icon[] icon_img = { icon1, icon2, icon3, icon4, icon5 };
		userList.setCellRenderer(new ImageList(icon_img));
	}

	// 一下为自动创建的window窗口关闭方法，暂时只用一个
	@Override
	public void windowClosing(WindowEvent arg0) {
		Customer.closeWindow(list.split("-")[0]);
		j = 0;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO 自动生成的方法存根

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO 自动生成的方法存根

	}
}
