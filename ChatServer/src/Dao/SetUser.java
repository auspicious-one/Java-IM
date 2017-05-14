package Dao;

import vo.User;

public class SetUser {
	public static User setType(int j, String tel, String name, String qianming, String sex, int k) {
		User user = new User();
		if (tel!=null) {
			user.setType(j);
			user.setTel(tel);
			user.setAccount(name);
			user.setQianming(qianming);
			user.setSex(sex);
			user.setAdmin(k);
		}
		return user;
	}
}
