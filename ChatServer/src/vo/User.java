package vo;

import java.io.Serializable;

public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int type;
	private String tel;
	private String account;
	private String qianming;
	private String sex;
	private int admin;
	public void setType(int j) {
		this.type = j;
	}

	public int getType() {
		return type;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getQianming() {
		return qianming;
	}

	public void setQianming(String qianming) {
		this.qianming = qianming;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}
}
