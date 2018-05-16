package com.ccran.entity;

/**
 * 
* @ClassName: CSDNAuthor 
* @Description: CSDN博主实体
* @author chenran
* @date 2018年5月14日 上午10:42:43 
* @version V1.0
 */
public class CSDNAuthor {
	private String authorName;
	private int blogNum;
	private int fansNum;
	private int likeNum;
	private int commentNum;
	private int levelNum;
	private int visitNum;
	private int integral;
	private int rank;
	
	public CSDNAuthor(){}
	
	public CSDNAuthor(String authorName, int blogNum, int fansNum, int likeNum, int commentNum, int level, int visitNum,
			int integral, int rank) {
		super();
		this.authorName = authorName;
		this.blogNum = blogNum;
		this.fansNum = fansNum;
		this.likeNum = likeNum;
		this.commentNum = commentNum;
		this.levelNum = level;
		this.visitNum = visitNum;
		this.integral = integral;
		this.rank = rank;
	}
	
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public int getBlogNum() {
		return blogNum;
	}
	public void setBlogNum(int blogNum) {
		this.blogNum = blogNum;
	}
	public int getFansNum() {
		return fansNum;
	}
	public void setFansNum(int fansNum) {
		this.fansNum = fansNum;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public int getCommentNum() {
		return commentNum;
	}
	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
	}
	public int getLevelNum() {
		return levelNum;
	}
	public void setLevelNum(int level) {
		this.levelNum = level;
	}
	public int getVisitNum() {
		return visitNum;
	}
	public void setVisitNum(int visitNum) {
		this.visitNum = visitNum;
	}
	public int getIntegral() {
		return integral;
	}
	public void setIntegral(int integral) {
		this.integral = integral;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@Override
	public String toString() {
		return "博主账号名:"+authorName+" 博客数量:"+blogNum+" 博主粉丝:"+fansNum+
				" 博主喜欢:"+likeNum+" 博主评论量:"+commentNum+" 博主等级:"+levelNum+
				" 博主访问量:"+visitNum+" 博主积分:"+integral+" 博客排名:"+rank;
	}
}
