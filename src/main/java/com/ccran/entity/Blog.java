package com.ccran.entity;

import java.util.Date;

/**
 * 
* @ClassName: Blog 
* @Description: 博客实体
* @author chenran
* @date 2018年4月8日 上午10:39:06 
* @version V1.0
 */
public class Blog {
	private int blogId;
	private String title; 
	private String type;
	private String tag;
	private int authorId;
	private Date publish;
	private int readNum;
	
	public int getBlogId() {
		return blogId;
	}
	public void setBlogId(int blogId) {
		this.blogId = blogId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public Date getPublish() {
		return publish;
	}
	public void setPublish(Date publish) {
		this.publish = publish;
	}
	public int getReadNum() {
		return readNum;
	}
	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}
}
