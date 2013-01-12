package com.chenjw.spider.dt.model;

public class UserTokenModel implements java.io.Serializable {

	private static final long serialVersionUID = 749337812406985921L;

	private String id; // 用户UID

	private String token;// 用户token

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
