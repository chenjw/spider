package com.chenjw.spider.dt.dataobject;

import java.util.Date;

public class TweetDO {

	private String tid;
	// 微博发送者的id
	private String userId;
	// 微博关注者的id
	private String memberUserId;
	// 发送时间
	private Date postDate;
	private byte[] content;

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(String memberUserId) {
		this.memberUserId = memberUserId;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
