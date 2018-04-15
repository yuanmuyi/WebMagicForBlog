package com.ccran.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ccran.entity.Blog;
import com.ccran.wmblog.CnblogPageProcesser;

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
	public static final String DBURL = "jdbc:mysql://localhost:3306/cnblog?useSSL=false&useUnicode=true&characterEncoding=utf8&serverTimezone=UTC";
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
	* @Title: InsertIntoBlog 
	* @Description: 插入博客表
	* @param @param blogId 博客Id
	* @param @param title 博客题目
	* @param @param authorId 作者Id
	* @param @param publish 发布时间串
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoBlog(String urlStr,int blogId,String title,int authorId,String publish){
		try {
			PreparedStatement pps=con.prepareStatement("insert into blog(blogId,title,authorId,publish,url) values(?,?,?,?,?)");
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
	* @Title: InsertIntoAuthor 
	* @Description: 插入作者表
	* @param @param authorId 作者Id
	* @param @param authorName 作者账号名
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoAuthor(int authorId,String authorName,String urlStr){
		try {
			PreparedStatement pps=con.prepareStatement("insert into author(authorId,authorName,url) values(?,?,?)");
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
	* @Title: InsertReadNumIntoBlog 
	* @Description: 设置阅读量
	* @param @param blogId 博客Id
	* @param @param readNum 阅读量
	* @return void
	* @version V1.0
	 */
	public static void InsertReadNumIntoBlog(int blogId,int readNum){
		try {
			PreparedStatement pps=con.prepareStatement("update blog set readNum=? where blogId=?");
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
	* @Title: InsertTypeTagIntoBlog 
	* @Description: 设置博客类型以及标签
	* @param @param blogId 博客Id
	* @param @param type 博客类型
	* @param @param tag 博客标签
	* @return void
	* @version V1.0
	 */
	public static void InsertTypeTagIntoBlog(int blogId,String type,String tag){
		try {
			PreparedStatement pps=con.prepareStatement("update blog set type=?,tag=? where blogId=?");
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
	* @Title: InsertIntoAuthor 
	* @Description: TODO
	* @param @param authorName
	* @param @param authorNickName
	* @param @param createDate
	* @param @param fans
	* @param @param attention
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoAuthor(String authorName,String authorNickName,String createDate,int fans,int attention){
		try {
			PreparedStatement pps=con.prepareStatement("update author set authorNickName=?,createDate=?,fans=?,attention=? where authorName=?");
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
	
	public static void main(String[] args) throws Exception {
		/**
		 * 插入
		 */
		PreparedStatement pps1=con.prepareStatement("insert into author(authorId,authorName,authorNickName,createDate) values(4,'man','woman','2018-01-22 17:00')");
		pps1.executeUpdate();
		pps1.close();
		/**
		 * 查询
		 */
		PreparedStatement pps2=con.prepareStatement("select * from author");
		ResultSet rs=pps2.executeQuery();
		while(rs.next()){
			System.out.println(rs.getInt(1)+" "+rs.getString(2)+" "+rs.getString(3)+" "+rs.getDate(4)+" "+rs.getInt(5)+" "+rs.getInt(6));
		}
		rs.close();
		pps2.close();
	}
	
	
}
