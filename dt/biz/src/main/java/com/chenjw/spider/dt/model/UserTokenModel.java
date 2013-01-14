package com.chenjw.spider.dt.model;

import java.util.Date;

import com.chenjw.spider.dt.constants.UserStatusEnum;

public class UserTokenModel implements java.io.Serializable {

	private static final long serialVersionUID = 749337812406985921L;

	private String userId; // 用户UID

	private String token;// 用户token
	private String screenName;// 用户昵称
	private UserStatusEnum status;// 用户状态
	private Date expireDate;// 用户状态

	public boolean isValid() {
		if (status == UserStatusEnum.FOREVER_VALID) {
			return true;
		} else if (status == UserStatusEnum.PERIOD_VALID) {
			if (System.currentTimeMillis() < expireDate.getTime()) {
				return true;
			}
		}
		return false;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public UserStatusEnum getStatus() {
		return status;
	}

	public void setStatus(UserStatusEnum status) {
		this.status = status;
	}

}
