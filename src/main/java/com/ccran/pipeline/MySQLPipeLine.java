package com.ccran.pipeline;

import com.ccran.entity.CSDNAuthor;
import com.ccran.entity.CSDNBlog;
import com.ccran.entity.CnblogAuthor;
import com.ccran.entity.CnblogBlog;
import com.ccran.tools.DatabaseTool;

import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * 
* @ClassName: MySQLPipeLine 
* @Description: 用于将Processor数据持久化到MySQL
* @author chenran
* @date 2018年4月24日 下午3:48:52 
* @version V1.0
 */
public class MySQLPipeLine implements Pipeline {

	public void process(ResultItems resultItems, Task task) {
		processCnblog(resultItems,task);
		processCSDN(resultItems,task);
	}
	
	private void processCSDN(ResultItems resultItems, Task task){
		CSDNAuthor author=resultItems.get("csdn_author");
		CSDNBlog blog=resultItems.get("csdn_blog");
		int authorId=-1;
		if(author!=null){
			authorId=DatabaseTool.getCSDNAuthorIdByName(author.getAuthorName());
			//该博主记录不存在则进行插入
			if(authorId==-1){
				DatabaseTool.InsertIntoCSDNAuthor(author);
				authorId=DatabaseTool.getCSDNAuthorIdByName(author.getAuthorName());
			}
			System.out.println(authorId);
		}
		if(blog!=null){
			blog.setAuthorId(authorId);
			System.out.println(blog);
			DatabaseTool.InsertIntoCSDNBlog(blog);
		}
	}
	
	private void processCnblog(ResultItems resultItems, Task task){
		//博客园持久化
		CnblogBlog blog=resultItems.get("cnblog_blog");
		CnblogAuthor author=resultItems.get("cnblog_author");
		if(author!=null){
			DatabaseTool.InsertIntoCnblogAuthor(author);
		}
		if(blog!=null){
			DatabaseTool.InsertIntoCnblogBlog(blog);
		}	
	}
}
