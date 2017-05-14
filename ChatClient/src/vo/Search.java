package vo;

public class Search {
    private String account;
    private int userid;
    private String sex;
    private String qianming;
    private String tel;
    public String getAccount(){
 	   return account;
    }
    public void setAccount(String account){
 	   this.account=account;
    }
    public int getUserid(){
 	   return userid;
    }
    public void setUserid(int userid){
 	   this.userid=userid;
    }

	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getQianming() {
		return qianming;
	}
	public void setQianming(String qianming) {
		this.qianming = qianming;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}     
}
