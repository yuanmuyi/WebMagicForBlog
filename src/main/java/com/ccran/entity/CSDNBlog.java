package com.ccran.entity;

/**
 * 
* @ClassName: CSDNBlog 
* @Description: CSDN博客实体
* @author chenran
* @date 2018年5月14日 上午10:51:31 
* @version V1.0
 */
public class CSDNBlog {
	private int blogId;
	private String title;
	private int readNum;
	private String publishTime;
	private String tag;
	private int authorId;
	
	public CSDNBlog(){}
	
	public CSDNBlog(int blogId, String title, int readNum, String publishTime, String tag, int authorId) {
		super();
		this.blogId = blogId;
		this.title = title;
		this.readNum = readNum;
		this.publishTime = publishTime;
		this.tag = tag;
		this.authorId = authorId;
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
	public int getReadNum() {
		return readNum;
	}
	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
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

	@Override
	public String toString() {
		return "博客ID:"+blogId+" 博客标题:"+title+" 博客阅读量:"+readNum+" 博客发表时间:"+publishTime
				+" 博客标签:"+tag+" 博主ID:"+authorId;
	}
}
