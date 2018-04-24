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
	private String publish;
	private int readNum;
	private String url;
	private int flag;
	
	public static final int FLAG_PART=1;
	public static final int FLAG_TYPE_TAG=2;
	public static final int FLAG_READ_NUM=3;
	
	public Blog(){}
	
	public Blog(Builder build){
		blogId=build.blogId;
		title=build.title;
		type=build.type;
		tag=build.tag;
		authorId=build.authorId;
		publish=build.publish;
		readNum=build.readNum;
		url=build.url;
		flag=build.flag;
	}
	
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
	public String getPublish() {
		return publish;
	}
	public void setPublish(String publish) {
		this.publish = publish;
	}
	public int getReadNum() {
		return readNum;
	}
	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	
	//建造者
	public static class Builder{
		//必要参数
		private final int blogId;
		private final int flag;
		
		private String title; 
		private String type;
		private String tag;
		private int authorId;
		private String publish;
		private int readNum;
		private String url;
		
		public Builder(int blogId,int flag){
			this.blogId=blogId;
			this.flag=flag;
		}
		
		public Builder title(String val){
			title=val; return this;
		}
		
		public Builder type(String val){
			type=val; return this;
		}
		
		public Builder tag(String val){
			tag=val; return this;
		}
		
		public Builder authorId(int val){
			authorId=val; return this;
		}
		
		public Builder publish(String val){
			publish=val; return this;
		}
		
		public Builder readNum(int val){
			readNum=val; return this;
		}
		
		public Builder url(String val){
			url=val; return this;
		}
		
		public Blog build(){
			return new Blog(this);
		}
	}
}
