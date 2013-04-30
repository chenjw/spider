package com.chenjw.spider.hacktools.model;


public class AccountModel implements java.io.Serializable {

	private static final long serialVersionUID = -1569043811217999033L;
	private String loginId;
	private String password;
	private String nick;
	private String email;
	private String md5;
	
	public void trim(){
		if(loginId!=null){
			loginId=loginId.trim();
		}
		if(password!=null){
			password=password.trim();
		}
		if(nick!=null){
			nick=nick.trim();
		}
		if(email!=null){
			email=email.trim();
		}
		if(md5!=null){
			md5=md5.trim();
		}
	}
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}
	
	
}
