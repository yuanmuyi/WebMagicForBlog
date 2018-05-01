package com.ccran.entity;

/**
 * 
* @ClassName: CnblogAuthor 
* @Description: 作者信息实体
* @author chenran
* @date 2018年4月2日 上午11:49:35 
* @version V1.0
 */
public class CnblogAuthor {
	private int authorId;
	private String authorName;
	private String authorNickName;
	private String createDate;
	private int fans;
	private int attention;
	private String url;
	private int flag;
	
	public static final int FLAG_PART_1=1;
	public static final int FLAG_PART_2=2;
	
	public CnblogAuthor(){}
	public CnblogAuthor(Builder builder){
		authorId=builder.authorId;
		authorName=builder.authorName;
		authorNickName=builder.authorNickName;
		createDate=builder.createDate;
		fans=builder.fans;
		attention=builder.attention;
		url=builder.url;
		flag=builder.flag;
	}
	
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public String getAuthorName() {
		return authorName;
	}
	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}
	public String getAuthorNickName() {
		return authorNickName;
	}
	public void setAuthorNickName(String authorNickName) {
		this.authorNickName = authorNickName;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
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
	
	@Override
	public String toString() {
		return "博主ID:"+authorId+"博主账号名:"+authorName+"博主昵称:"+authorNickName+"账号创建日期:"+
				createDate+"粉丝量:"+fans+"关注量:"+attention;
	}
	
	//建造者
	public static class Builder{
		private final int flag;
		private final int authorId;
		private String authorName;
		private String authorNickName;
		private String createDate;
		private int fans;
		private int attention;
		private String url;
		
		public Builder(int authorId,int flag){
			this.authorId=authorId;
			this.flag=flag;
		}
		
		public Builder authorName(String val){
			this.authorName=val; return this;
		}
		
		public Builder authorNickName(String val){
			this.authorNickName=val; return this;
		}
		
		public Builder createDate(String val){
			this.createDate=val; return this;
		}
		
		public Builder fans(int val){
			this.fans=val; return this;
		}
		
		public Builder attention(int val){
			this.attention=val; return this;
		}
		
		public Builder url(String val){
			this.url=val; return this;
		}
		
		public CnblogAuthor build(){
			return new CnblogAuthor(this);
		}
	}
}
