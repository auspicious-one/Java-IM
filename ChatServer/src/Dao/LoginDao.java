package Dao;

import sql.DBHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vo.*;

public class LoginDao {
	static Connection conn;

	// 用户登录
	public Login checkLogin(String tel, String password) {
		try {
			conn = DBHelper.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select userid,account,password,sex,qianming,tel,admin from t_customer where tel=?"
							+ "and password=?");
			pstmt.setString(1, tel);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Login login = new Login();
				login.setUserid(rs.getInt("userid"));
				login.setAccount(rs.getString("account"));
				login.setPassword(rs.getString("password"));
				login.setSex(rs.getString("sex"));
				login.setQianming(rs.getString("qianming"));
				login.setTel(rs.getString("tel"));
				login.setAdmin(rs.getInt("admin"));
				return login;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//用户注册
	public Register addUser(String tel, String password, String qianming, String name, String sex) throws Exception {
		conn = DBHelper.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select userid from t_customer where tel=?");
		pstmt.setString(1, tel);
		ResultSet rs = pstmt.executeQuery();
		Register rg = new Register();
		if (rs.next()) {
			rg.setCanRegister(false);
		} else {
			PreparedStatement ps = null;
			String sql = "insert into t_customer(account,password,sex,qianming,tel,join_time,admin) VALUES(?,?,?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, password);
			ps.setString(3, sex);
			ps.setString(4, qianming);
			ps.setString(5, tel);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			ps.setString(6, format.format(new Date()));
			int admin=(int) (Math.random() * 1);
			ps.setInt(7, admin);
			ps.executeUpdate();
			ps.close();
			rg.setCanRegister(true);
		}
		return rg;
	}
	
	// 查找登录用户的好友信息
	public ArrayList getAll(int id) throws Exception {
		try {
			conn = DBHelper.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select myfriends,account,qianming,sex,tel from t_friend "
					+ "inner join t_customer on t_friend.myfriends=t_customer.userid where t_friend.userid=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			ArrayList all = new ArrayList();
			while (rs.next()) {
				Friend friend = new Friend();
				friend.setUserid(rs.getInt("myfriends"));
				friend.setAccount(rs.getString("account"));
				friend.setQianming(rs.getString("qianming"));
				friend.setSex(rs.getString("sex"));
				friend.setTel(rs.getString("tel"));
				all.add(friend);
			}
			return all;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//保存离线聊天数据
	public static void Retain(String from_name,String from_num,String to,String info){
		try {
			conn = DBHelper.getConnection();
			PreparedStatement ps = null;
			String sql = "insert into t_info(from_name,from_num,to_num,info,date) VALUES(?,?,?,?,?)";
			ps = conn.prepareStatement(sql);
			ps.setString(1, from_name);
			ps.setString(2, from_num);
			ps.setString(3, to);
			ps.setString(4, info);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			ps.setString(5, format.format(new Date()));
			ps.executeUpdate();
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//查询当前用户的离线信息
	public  ArrayList outLineInfo(String num){
		try {
			conn = DBHelper.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select from_num,from_name,info,to_num,send from t_info where to_num=?");
			pstmt.setString(1, num);
			ResultSet rs = pstmt.executeQuery();
			ArrayList all = new ArrayList();
			while (rs.next()) {
				if(rs.getInt("send")==0){
					outLineInfo out=new outLineInfo();
					out.setFrom_name(rs.getString("from_name"));
					out.setFrom_num(rs.getString("from_num"));
					out.setInfo(rs.getString("info"));
					out.setTo_num(rs.getString("to_num"));
					all.add(out);
				}
			}
			pstmt.close();
			PreparedStatement ps = conn.prepareStatement("update t_info set send='1' where to_num=?");
			ps.setString(1, num);
			ps.execute();
			ps.close();
			return all;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public ArrayList Search(String info) throws Exception{
		conn = DBHelper.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("select userid,account,password,sex,qianming,tel,admin"
				+ " from t_customer where account like ? or tel like ? or qianming like ?");
		pstmt.setString(1, "%"+info+"%");
		pstmt.setString(2, "%"+info+"%");
		pstmt.setString(3, "%"+info+"%");
		ResultSet rs = pstmt.executeQuery();
		ArrayList all = new ArrayList();
		while (rs.next()) {
				Login login=new Login();
				login.setUserid(rs.getInt("userid"));
				login.setAccount(rs.getString("account"));
				login.setPassword(rs.getString("password"));
				login.setSex(rs.getString("sex"));
				login.setQianming(rs.getString("qianming"));
				login.setTel(rs.getString("tel"));
				login.setAdmin(rs.getInt("admin"));
				all.add(login);
		}
		pstmt.close();
		return all;
		
	}
	public static void main(String [] args) throws Exception{
		LoginDao login=new LoginDao();
		ArrayList list=login.Search("113");
	}
}
