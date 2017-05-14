package Server;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.List;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import vo.*;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ImageList.ImageList;
import Login.LoginView;
import Main.BgWindow;
import Main.InfoImage;
import Window.ChatWindow;
import Window.PersonInfo;

import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

public class Customer implements ActionListener {
	private JFrame jframe;
	private static final long serialVersionUID = -5832302420914722347L;
	private JPanel plAll = new JPanel();
	private static List allChat = new List();

	private JPanel plOne = new JPanel();
	private static List oneChat = new List();

	private JPanel plSend = new JPanel(new BorderLayout());
	private JTextArea sendMsg = new JTextArea();
	private JButton btnSend = new JButton("发送");
	private JButton sendAll = new JButton("创建全体消息");
	private JPanel plSearch = new JPanel();

	private JPanel plUser_msg = new JPanel();
	private static JTabbedPane tab;
	static String line = null;
	static User user = null;
	static ArrayList<String> list = new ArrayList<String>(); // 保存打开的窗口信息
	static int all_info = 0; // 设置收到的所有人信息的条数
	static int one_info = 0; // 设置收到的个人信息得到条数
	static JList userList;
	static DefaultListModel listModel;
	static int j = 0; // 列表项
	private JScrollPane js;
	private final JTextField search = new JTextField();
	private static List search_list;
	private static JButton refresh = new JButton();
	private static ArrayList<Search> search_info = new ArrayList<Search>();
	private static ArrayList<AllUser> AllUserList = new ArrayList<AllUser>();

	public Customer(User user) {
		search.setHorizontalAlignment(SwingConstants.CENTER);
		search.setBounds(0, 0, 269, 26);
		search.setBackground(Color.WHITE);
		search.setColumns(10);
		this.user = user;
		jframe = new JFrame();
		BgWindow.WidnowBackground(jframe);

		jframe.getContentPane().setBackground(Color.WHITE);
		plSend.add(sendMsg, BorderLayout.CENTER);
		plSend.add(btnSend, BorderLayout.EAST);
		plUser_msg.setBackground(Color.WHITE);
		plUser_msg.setBounds(0, 0, 181, 392);
		plUser_msg.setLayout(null);
		plOne.setBackground(Color.WHITE);
		plOne.setBounds(380, 0, 200, 392);
		plOne.setLayout(null);
		oneChat.setEnabled(false);
		oneChat.setBackground(Color.WHITE);
		oneChat.select(-1);
		oneChat.setBounds(30, 180, 203, 283);
		oneChat.add("");
		oneChat.add("账号：" + user.getTel());
		oneChat.add("");
		oneChat.add("昵称：" + user.getAccount());
		oneChat.add("");
		oneChat.add("签名：" + user.getQianming());
		oneChat.add("");
		oneChat.add("性别：" + user.getSex());
		oneChat.add("");
		if (user.getAdmin() == 0) {
			oneChat.add("管理员权限：无");
		} else {
			oneChat.add("管理员权限：已获得");
		}
		plOne.add(oneChat);

		JLabel label = new JLabel(InfoImage.InfoImage());
		label.setBackground(Color.WHITE);
		label.setBounds(30, 30, 203, 120);

		plOne.add(label);
		plAll.setBackground(Color.WHITE);
		plAll.setBounds(181, 0, 200, 392);
		plAll.setLayout(null);
		allChat.setMultipleMode(true);
		allChat.setBounds(0, 22, 269, 495);
		plAll.add(allChat);

		JLabel label_1 = new JLabel("\u5168\u4F53\u6D88\u606F\u8BB0\u5F55");
		label_1.setForeground(Color.DARK_GRAY);
		label_1.setBackground(Color.WHITE);
		label_1.setBounds(0, 1, 93, 22);
		plAll.add(label_1);

		sendAll.setBounds(0, 518, 269, 33);
		sendAll.addActionListener(this);
		sendAll.setActionCommand("all");
		plAll.add(sendAll);
		jframe.getContentPane().setLayout(null);

		tabView(); // 选项卡布局
		ListView(); // 好友列表布局

		jframe.setSize(300, 633);
		jframe.setVisible(true);
		jframe.setResizable(false);
		jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		jframe.setTitle(user.getAccount());
		// jframe.setLocationRelativeTo(jframe.getOwner());
		jframe.setLocation(950, 50);
		jframe.addWindowListener(new WindowListener() {
			public void windowIconified(WindowEvent arg0) {
				jframe.dispose();
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void windowClosed(WindowEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO 自动生成的方法存根

			}

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO 自动生成的方法存根

			}
		});
		this.mini();
	}

	// 选项卡布局
	public void tabView() {
		tab = new JTabbedPane(JTabbedPane.TOP);
		tab.setForeground(Color.BLACK);
		tab.setBackground(Color.WHITE);
		tab.setBounds(10, 0, 274, 594);

		Icon icon_chat = new ImageIcon("main_icon/main_icon_chat2.jpg");
		Icon icon_all = new ImageIcon("main_icon/main_icon_all.jpg");
		Icon icon_user = new ImageIcon("main_icon/main_icon_user.jpg");
		Icon icon_search = new ImageIcon("main_icon/main_icon_search.jpg");
		tab.addTab("   ", icon_chat, plUser_msg);
		tab.addTab("   ", icon_all, plAll);
		tab.addTab("   ", icon_user, plOne);
		plSearch.setBackground(Color.WHITE);
		tab.addTab("   ", icon_search, plSearch);
		plSearch.setLayout(null);

		plSearch.add(search);

		JButton button = new JButton("查询");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!search.getText().equals("")) {
					Search();
				}
			}
		});
		button.setBounds(203, 58, 66, 26);
		plSearch.add(button);

		JRadioButton radioButton = new JRadioButton("\u5E10\u6237");
		radioButton.setSelected(true);
		radioButton.setBackground(Color.WHITE);
		radioButton.setBounds(0, 30, 49, 23);
		plSearch.add(radioButton);

		JRadioButton radioButton_1 = new JRadioButton("\u6635\u79F0");
		radioButton_1.setBackground(Color.WHITE);
		radioButton_1.setBounds(100, 30, 49, 23);
		plSearch.add(radioButton_1);

		JRadioButton radioButton_2 = new JRadioButton("\u7B7E\u540D");
		radioButton_2.setBackground(Color.WHITE);
		radioButton_2.setBounds(200, 32, 49, 23);
		plSearch.add(radioButton_2);

		search_list = new List();
		search_list.setBounds(0, 92, 269, 473);
		search_list.addActionListener(this);
		plSearch.add(search_list);

		tab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				int index = tab.getSelectedIndex();
				if (index == 0) {
					tab.setTitleAt(0, "   ");
				} else if (index == 1) {
					tab.setTitleAt(1, "   ");
				}
			}

		});
		jframe.getContentPane().add(tab);
	}

	// 好友list的布局
	public void ListView() {
		listModel = new DefaultListModel();
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int index = userList.locationToIndex(e.getPoint());
					Object object = userList.getSelectedValue();
					String list_data = (object.toString()).split(" ")[1];
					String num = list_data.split("-")[0];
					newWindow(num, "new", "", "", list_data);
				}
			}
		};
		userList = new JList(listModel);
		userList.setBackground(Color.WHITE);
		userList.setLocation(0, 0);
		userList.setSize(306, 565);
		userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		userList.addMouseListener(mouseListener);
		js = new JScrollPane(userList);
		js.setLocation(0, 0);
		js.setSize(269, 523);
		plUser_msg.add(js);

		refresh = new JButton("\u91CD\u65B0\u52A0\u8F7D");
		refresh.setBounds(0, 524, 269, 23);
		refresh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Refresh();
			}

		});
		plUser_msg.add(refresh);
	}

	// 获取登陆后信息并处理
	public static void recLogin_in(String line) {
		ChatWindow.recMessage(line); // 给窗口添加信息;
		AllUser allUser = new AllUser();
		try {
			JSONObject json = JSONObject.fromObject(line);
			if (json.getString("type").equals("all_friends")) {
				All_friends(json);
			} else if (json.getString("type").equals("message")) {
				Message(json);
			} else if (json.getString("type").equals("notice_login")) {
				j = 0;
				setIcon();
				listModel.removeElement(" " + json.getString("tel") + "-" + json.getString("name") + "(离线)");
				listModel.add(0, " " + json.getString("tel") + "-" + json.getString("name"));
				openWindow(json.getString("name"), "上线了", json.getString("tel"), "1");
			} else if (json.getString("type").equals("login_out")) {
				listModel.removeElement(" " + json.getString("tel") + "-" + json.getString("name"));
				listModel.add(j++, " " + json.getString("tel") + "-" + json.getString("name") + "(离线)");
			} else if (json.getString("type").equals("result_info")) {
				ResultInfo(json);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 重新加载好友数据
	public void Refresh() {
		j = 0;
		listModel.removeAllElements();
		if (AllUserList.size() > 0) {
			for (int i = 0; i < AllUserList.size(); i++) {
				AllUser allUser = AllUserList.get(i);
				if (allUser.getIn() == 1) {
					listModel.add(0, " " + allUser.getTel() + "-" + allUser.getAccount());
				} else {
					listModel.add(j++, " " + allUser.getTel() + "-" + allUser.getAccount() + "(离线)");
				}
			}
		}
	}

	// 接收消息的处理方法
	private static void Message(JSONObject json) {
		String from_num = json.getString("from_num");
		String from_name = json.getString("from_name");
		String to = json.getString("to");
		String info = json.getString("info");
		if (to.equals("All")) {
			if (!from_num.equals(user.getTel())) {
				all_info++;
				tab.setTitleAt(0, "     " + String.valueOf(all_info)); // 更新选项卡上群聊信息未读消息数目
			}
			allChat.add(from_name + "说:" + info + "\n");
			allChat.add("");
		} else {
			one_info++; // 更新选项卡上个人信息未读数目
			tab.setTitleAt(0, "     " + String.valueOf(one_info));
			newWindow(from_num, "old", from_name, info, "");
		}
	}

	// 用户列表处理方法
	private static void All_friends(JSONObject json) {
		listModel.removeAllElements();
		JSONArray array = json.getJSONArray("all");
		setIcon();
		for (int i = 0; i < array.size(); i++) {
			AllUser allUser = new AllUser();
			JSONObject info = array.getJSONObject(i);
			if (info.getInt("userId") != allUser.getUserid()) {
				allUser.setAccount(info.getString("name"));
				allUser.setQianming(info.getString("qianming"));
				allUser.setSex(info.getString("sex"));
				allUser.setTel(info.getString("tel"));
				allUser.setUserid(info.getInt("userId"));
				AllUserList.add(allUser);
				setIcon();
				if (info.getString("friend_online").equals("1")) {
					allUser.setIn(1);
					listModel.add(0, " " + allUser.getTel() + "-" + allUser.getAccount());
				} else {
					allUser.setIn(0);
					listModel.add(j++, " " + allUser.getTel() + "-" + allUser.getAccount() + "(离线)");
				}
			}
		}

	}

	// 加载搜索到的用户信息
	public static void ResultInfo(JSONObject json) {
		search_list.removeAll();
		JSONArray array = json.getJSONArray("result");
		if (array.size() == 0) {
			search_list.add("查询结果不存在!");
		}
		for (int i = 0; i < array.size(); i++) {
			JSONObject info = array.getJSONObject(i);
			search_list.add("昵称：" + info.getString("account") + "     账号:" + info.getString("tel"));
			search_list.add("");
			Search sea = new Search();
			sea.setAccount(info.getString("account"));
			sea.setQianming(info.getString("qianming"));
			sea.setTel(info.getString("tel"));
			sea.setSex(info.getString("sex"));
			search_info.add(sea);
		}
	}

	// 创建一个新的聊天窗口
	public static void newWindow(String num, String type, String from_name, String info, String list_data) {
		boolean isIn = false;
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals(num)) {
					isIn = true;
				}
			}
			// 判断当前是否存在，并且判断所选择的类型，old代表传入的是提示框，new代表选择用户打开对话框
			if (isIn == false && type.equals("new")) {
				new ChatWindow(list_data, line, user);
				list.add(num);
			} else if (isIn == false && type.equals("old")) {
				openWindow(from_name, info, num, "0");
			} else {
				tab.setTitleAt(0, "     ");
			}
		} else if (type.equals("new")) {
			new ChatWindow(list_data, line, user);
			list.add(num);
		} else if (type.equals("old")) {
			openWindow(from_name, info, num, "0");
		}
	}

	// 获得已关闭的窗口的值，在list中删除这个值
	public static void closeWindow(String num) {
		list.remove(num);
	}

	// type代表通知类型，1代表好友上线，0代表新信息
	public static void openWindow(String name, String info, String num, String type) {
		Object[] options = { "打开聊天框", "忽略此消息" };
		String img[] = { "icon1", "icon2", "icon3", "icon4", "icon5" };
		int i = (int) (Math.random() * 4) + 1;
		Icon icon = new ImageIcon("new_info/" + img[i] + ".jpg");
		JOptionPane pane = new JOptionPane(name + "：" + info, JOptionPane.YES_OPTION, JOptionPane.NO_OPTION, icon,
				options);

		JDialog dialog;
		if (type.equals("1")) {
			dialog = pane.createDialog(null, "好友上线提醒");
		} else {
			dialog = pane.createDialog(null, "新消息提醒");
		}
		dialog.show();
		Object s = pane.getValue();
		if (s != null && s.equals("打开聊天框")) {
			setIcon();
			String info_list = num + "-" + name;
			new ChatWindow(info_list, line, user);
			list.add(num);
			ChatWindow.info_new(name, info);
		}
	}

	// 查询好友的方法
	private void Search() {
		JSONObject json = new JSONObject();
		json.put("type", "search");
		json.put("from_num", user.getTel());
		json.put("info", search.getText());
		LoginView.SendMessage(json);
	}

	// 给好友列表分配头像
	public static void setIcon() {
		Icon icon1 = new ImageIcon("new_info/icon1.jpg");
		Icon icon2 = new ImageIcon("new_info/icon2.jpg");
		Icon icon3 = new ImageIcon("new_info/icon3.jpg");
		Icon icon4 = new ImageIcon("new_info/icon4.jpg");
		Icon icon5 = new ImageIcon("new_info/icon5.jpg");
		// Icon[] icon_img = {new ImageIcon("new_info/" + img[i] + ".jpg")};
		Icon[] icon_img = { icon1, icon2, icon3, icon4, icon5 };
		userList.setCellRenderer(new ImageList(icon_img));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("all")) {
			if (user.getAdmin() == 1) {
				newWindow("", "new", "", "", "All-");
			} else {
				JOptionPane.showMessageDialog(null, "对不起，您并没有管理员权限!");
			}
		} else {
			if (!search_list.getSelectedItem().equals("")) {
				new PersonInfo(search_info, search_list.getSelectedItem().split(":")[1], AllUserList, user);
			}
		}
	}

	public void mini() {
		SystemTray tray = SystemTray.getSystemTray();
		// 创建托盘图标：1.显示图标Image 2.停留提示text 3.弹出菜单popupMenu 4.创建托盘图标实例
		// 1.创建Image图像
		Image image = Toolkit.getDefaultToolkit().getImage("main_icon/icon.jpg");
		// 2.停留提示text
		String text = user.getAccount();
		PopupMenu popMenu = new PopupMenu();
		MenuItem itmOpen = new MenuItem("打开");
		itmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Show();
			}
		});
		MenuItem itmHide = new MenuItem("隐藏");
		itmHide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				UnVisible();
			}
		});
		MenuItem itmExit = new MenuItem("退出");
		itmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Exit();
			}
		});
		popMenu.add(itmOpen);
		popMenu.add(itmHide);
		popMenu.add(itmExit);

		TrayIcon trayIcon = new TrayIcon(image, text, popMenu);
		trayIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Show();
			}
		});
		// 将托盘图标加到托盘上
		try {
			tray.add(trayIcon);
		} catch (AWTException e1) {
			e1.printStackTrace();
		}

	}

	public void UnVisible() {
		jframe.setVisible(false);
	}

	public void Show() {
		jframe.setVisible(true);
	}

	public void Exit() {
		System.exit(0);
	}
}
