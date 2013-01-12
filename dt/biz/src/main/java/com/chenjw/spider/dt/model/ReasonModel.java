package com.chenjw.spider.dt.model;

import java.util.Date;

public class ReasonModel {
	private String text;
	private String thumbnailPic; // 微博内容中的图片的缩略地址
	private Date createdAt;
	private String id;
	private UserModel user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getThumbnailPic() {
		return thumbnailPic;
	}

	public void setThumbnailPic(String thumbnailPic) {
		this.thumbnailPic = thumbnailPic;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}
