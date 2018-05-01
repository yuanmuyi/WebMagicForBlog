package com.ccran.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ccran.entity.CnblogAuthor;
import com.ccran.entity.CnblogBlog;
import com.ccran.processor.CnblogPageProcesser;

/**
 * 
* @ClassName: DatabaseTools 
* @Description: 数据库处理
* @author chenran
* @date 2018年4月10日 上午11:08:58 
* @version V1.0
 */
public class DatabaseTools {
	/**
	 * @param args
	 */
	private static Logger logger=Logger.getLogger(DatabaseTools.class);
	//驱动程序
	public static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";
	//连接地址是由各个数据库生产商单独提供的，所以需要单独记住
	public static final String DBURL = "jdbc:mysql://localhost:3306/blog?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
	//连接数据库的用户名
	public static final String DBUSER = "root";
	//连接数据库的密码
	public static final String DBPASS = "root";
	//mysql连接
	private static Connection con=null;
	static{
		try{
			Class.forName(DBDRIVER); //1、使用CLASS 类加载驱动程序
			con = DriverManager.getConnection(DBURL,DBUSER,DBPASS); //2、连接数据库
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: existCnblogBlogItem 
	* @Description: 判断数据库表中是否存在博文Id为blogId的记录
	* @param @param blogId
	* @param @return
	* @return boolean
	* @version V1.0
	 */
	private static boolean existCnblogBlogItem(int blogId){
		try{
			PreparedStatement pps=con.prepareStatement("select * from cnblog_blog where blogId=?");
			pps.setInt(1, blogId);
			ResultSet rs=pps.executeQuery();
			if(rs.next()){
				return true;
			}
		}catch(Exception e){
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	* @Title: existCnblogAuthorItem 
	* @Description: 判断数据库表cnblog_author表中是否存在博主Id为authorId的记录
	* @param @param authorId
	* @param @return
	* @return boolean
	* @version V1.0
	 */
	private static boolean existCnblogAuthorItem(int authorId){
		try{
			PreparedStatement pps=con.prepareStatement("select * from cnblog_author where authorId=?");
			pps.setInt(1, authorId);
			ResultSet rs=pps.executeQuery();
			if(rs.next()){
				return true;
			}
		}catch(Exception e){
			logger.error(e);
		}
		return false;
	}
	
	/**
	 * 
	* @Title: InsertIntoCnblogBlog 
	* @Description: 通过博客对象插入博客表
	* @param @param blog
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoCnblogBlog(CnblogBlog blog){
		switch(blog.getFlag()){
		case CnblogBlog.FLAG_PART:
			//不存在博文信息才可以完成插入
			if(!DatabaseTools.existCnblogBlogItem(blog.getBlogId()))
				InsertIntoCnblogBlog(blog.getUrl(),blog.getBlogId(),blog.getTitle(),blog.getAuthorId(),blog.getPublish());
			break;
		case CnblogBlog.FLAG_TYPE_TAG:
			UpdateTypeTagInCnblogBlog(blog.getBlogId(),blog.getType(),blog.getTag());
			break;
		case CnblogBlog.FLAG_READ_NUM:
			UpdateReadNumInCnblogBlog(blog.getBlogId(),blog.getReadNum());
			break;
		}
	}
	
	/**
	 * 
	* @Title: InsertIntoCnblogBlog 
	* @Description: 插入博客表
	* @param @param blogId 博客Id
	* @param @param title 博客题目
	* @param @param authorId 作者Id
	* @param @param publish 发布时间串
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoCnblogBlog(String urlStr,int blogId,String title,int authorId,String publish){
		try {
			PreparedStatement pps=con.prepareStatement("insert into cnblog_blog(blogId,title,authorId,publish,url) values(?,?,?,?,?)");
			pps.setInt(1, blogId);
			pps.setString(2, title);
			pps.setInt(3, authorId);
			pps.setString(4, publish);
			pps.setString(5, urlStr);
			pps.executeUpdate();
			pps.close();
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: InsertReadNumIntoCnblogBlog 
	* @Description: 设置阅读量
	* @param @param blogId 博客Id
	* @param @param readNum 阅读量
	* @return void
	* @version V1.0
	 */
	public static void UpdateReadNumInCnblogBlog(int blogId,int readNum){
		try {
			PreparedStatement pps=con.prepareStatement("update cnblog_blog set readNum=? where blogId=?");
			pps.setInt(1, readNum);
			pps.setInt(2, blogId);
			pps.executeUpdate();
			pps.close();
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: InsertTypeTagIntoCnblogBlog 
	* @Description: 设置博客类型以及标签
	* @param @param blogId 博客Id
	* @param @param type 博客类型
	* @param @param tag 博客标签
	* @return void
	* @version V1.0
	 */
	public static void UpdateTypeTagInCnblogBlog(int blogId,String type,String tag){
		try {
			PreparedStatement pps=con.prepareStatement("update cnblog_blog set type=?,tag=? where blogId=?");
			pps.setString(1, type);
			pps.setString(2, tag);
			pps.setInt(3, blogId);
			pps.executeUpdate();
			pps.close();
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: InsertIntoCnblogAuthor 
	* @Description: 根据作者类插入数据库
	* @param @param author
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoCnblogAuthor(CnblogAuthor author){
		switch(author.getFlag()){
		case CnblogAuthor.FLAG_PART_1:
			//不存在作者信息才能插入
			if(!DatabaseTools.existCnblogAuthorItem(author.getAuthorId()))
				InsertIntoCnblogAuthor(author.getAuthorId(), author.getAuthorName(), author.getUrl());
			break;
		case CnblogAuthor.FLAG_PART_2:
			UpdateCnblogAuthor(author.getAuthorName(), author.getAuthorNickName(), author.getCreateDate(), author.getFans(), author.getAttention());
			break;
		}
	}
	
	/**
	 * 
	* @Title: InsertIntoCnblogAuthor 
	* @Description: 插入作者表
	* @param @param authorId 作者Id
	* @param @param authorName 作者账号名
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoCnblogAuthor(int authorId,String authorName,String urlStr){
		try {
			PreparedStatement pps=con.prepareStatement("insert into cnblog_author(authorId,authorName,url) values(?,?,?)");
			pps.setInt(1, authorId);
			pps.setString(2, authorName);
			pps.setString(3, urlStr);
			pps.executeUpdate();
			pps.close();
		} catch (SQLException e) {
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: InsertIntoCnblogAuthor 
	* @Description: TODO
	* @param @param authorName
	* @param @param authorNickName
	* @param @param createDate
	* @param @param fans
	* @param @param attention
	* @return void
	* @version V1.0
	 */
	public static void UpdateCnblogAuthor(String authorName,String authorNickName,String createDate,int fans,int attention){
		try {
			PreparedStatement pps=con.prepareStatement("update cnblog_author set authorNickName=?,createDate=?,fans=?,attention=? where authorName=?");
			pps.setString(1, authorNickName);
			pps.setString(2, createDate);
			pps.setInt(3, fans);
			pps.setInt(4, attention);
			pps.setString(5, authorName);
			pps.executeUpdate();
			pps.close();
		} catch (SQLException e) {
			logger.error(e);
		}		
	}
	
	public static void main(String[] args) {
		System.out.println(existCnblogAuthorItem(73901));
	}
}
