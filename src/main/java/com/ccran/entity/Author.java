package com.ccran.entity;

import java.util.Date;

/**
 * 
* @ClassName: Author 
* @Description: 作者信息实体
* @author chenran
* @date 2018年4月2日 上午11:49:35 
* @version V1.0
 */
public class Author {
	private int authorId;
	private String authorName;
	private String authorNickName;
	private Date createDate;
	private int fans;
	private int attention;
	
	public int getUserId() {
		return authorId;
	}
	public void setUserId(int userId) {
		this.authorId = userId;
	}
	public String getUserName() {
		return authorName;
	}
	public void setUserName(String userName) {
		this.authorName = userName;
	}
	public String getUserNickName() {
		return authorNickName;
	}
	public void setUserNickName(String userNickName) {
		this.authorNickName = userNickName;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public int getFans() {
		return fans;
	}
	public void setFans(int fans) {
		this.fans = fans;
	}
	public int getAttention() {
		return attention;
	}
	public void setAttention(int attention) {
		this.attention = attention;
	}
}
