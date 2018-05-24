package com.ccran.tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ccran.entity.CSDNAuthor;
import com.ccran.entity.CSDNBlog;
import com.ccran.entity.CnblogAuthor;
import com.ccran.entity.CnblogBlog;
import com.ccran.entity.IPProxyItem;

/**
 * 
* @ClassName: DatabaseTools 
* @Description: 数据库处理
* @author chenran
* @date 2018年4月10日 上午11:08:58 
* @version V1.0
 */
public class DatabaseTool {
	/**
	 * @param args
	 */
	private static Logger logger=Logger.getLogger(DatabaseTool.class);
	//驱动程序
	//6.0以上驱动名称
	public static final String DBDRIVER = "com.mysql.jdbc.Driver";
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
	* @Title: getCSDNAuthorIdByName 
	* @Description: 根据CSDN博主名称获取ID
	* @param @param authorName
	* @param @return
	* @return int
	* @version V1.0
	 */
	public static int getCSDNAuthorIdByName(String authorName){
		try{
			PreparedStatement pps=con.prepareStatement("select authorId from csdn_author where authorName=?");
			pps.setString(1, authorName);
			ResultSet rs=pps.executeQuery();
			if(rs.next()){
				return rs.getInt(1);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return -1;
	}
	
	/**
	 * 
	* @Title: InsertIntoCSDNAuthor 
	* @Description: 插入CSDN博主表
	* @param @param author
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoCSDNAuthor(CSDNAuthor author){
		try{
			PreparedStatement pps=con.prepareStatement("insert into csdn_author(authorName,blogNum,fansNum,likeNum,commentNum,levelNum,visitNum,integral,rank) values(?,?,?,?,?,?,?,?,?)");
			pps.setString(1, author.getAuthorName());
			pps.setInt(2, author.getBlogNum());
			pps.setInt(3, author.getFansNum());
			pps.setInt(4, author.getLikeNum());
			pps.setInt(5, author.getCommentNum());
			pps.setInt(6, author.getLevelNum());
			pps.setInt(7, author.getVisitNum());
			pps.setInt(8, author.getIntegral());
			pps.setInt(9, author.getRank());
			pps.executeUpdate();
			pps.close();
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: InsertIntoCSDNBlog 
	* @Description: 插入CSDN博文表
	* @param @param blog
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoCSDNBlog(CSDNBlog blog){
		try{
			PreparedStatement pps=con.prepareStatement("insert into csdn_blog(blogId,title,readNum,publishTime,tag,authorId) values(?,?,?,?,?,?)");
			pps.setInt(1, blog.getBlogId());
			pps.setString(2, blog.getTitle());
			pps.setInt(3, blog.getReadNum());
			pps.setString(4, blog.getPublishTime());
			pps.setString(5, blog.getTag());
			pps.setInt(6, blog.getAuthorId());
			pps.executeUpdate();
			pps.close();
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
			//不存在博客信息才可以完成插入
			if(!DatabaseTool.existCnblogBlogItem(blog.getBlogId())){
				InsertIntoCnblogBlog(blog.getUrl(),blog.getBlogId(),
				blog.getTitle(),blog.getAuthorId(),blog.getPublish());
			}break;
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
			if(!DatabaseTool.existCnblogAuthorItem(author.getAuthorId())){
				InsertIntoCnblogAuthor(author.getAuthorId(), 
				author.getAuthorName(), author.getUrl());
			}
			break;
		case CnblogAuthor.FLAG_PART_2:
			UpdateCnblogAuthor(author.getAuthorName(), 
			author.getAuthorNickName(), author.getCreateDate(), 
			author.getFans(), author.getAttention());
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
	
	/**
	 * 
	* @Title: InsertIntoIPProxy 
	* @Description: 插入IP代理记录进数据库
	* @param @param item
	* @return void
	* @version V1.0
	 */
	public static void InsertIntoIPProxy(IPProxyItem item){
		try{
			PreparedStatement pps=con.prepareStatement("insert into ipproxy(ipAddress,port,serverLocate,anonymity,type) values(?,?,?,?,?)");
			pps.setString(1, item.getIpAddress());
			pps.setInt(2, item.getPort());
			pps.setString(3, item.getServerLocate());
			pps.setInt(4,item.getAnonymity());
			pps.setString(5, item.getType());
			pps.executeUpdate();
			pps.close();
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	/**
	 * 
	* @Title: getIPProxyItemList 
	* @Description: TODO
	* @param @return
	* @return List<IPProxyItem>
	* @version V1.0
	 */
	public static List<IPProxyItem> getIPProxyItemList(){
		List<IPProxyItem> result=new ArrayList<IPProxyItem>();
		try{
			PreparedStatement pps=con.prepareStatement("select * from ipproxy");
			ResultSet resultSet=pps.executeQuery();
			while(resultSet.next()){
				String ipAddress=resultSet.getString(2);
				int port=resultSet.getInt(3);
				String serverLocate=resultSet.getString(4);
				int anonymity=resultSet.getInt(5);
				String type=resultSet.getString(6);
				IPProxyItem item=new IPProxyItem(ipAddress, port, serverLocate, anonymity, type);
				result.add(item);
			}
		}catch(Exception e){
			logger.error(e);
		}
		return result;
	}
	
	/**
	 * 
	* @Title: clearIPProxyTable 
	* @Description: 清空IPProxy表
	* @param 
	* @return void
	* @version V1.0
	 */
	public static void clearIPProxyTable(){
		try{
			PreparedStatement pps=con.prepareStatement("delete from ipproxy");
			pps.executeUpdate();
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	public static void main(String[] args) throws Exception{
		clearIPProxyTable();
	}
}
