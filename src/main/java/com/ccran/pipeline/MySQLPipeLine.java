package com.ccran.pipeline;

import com.ccran.entity.CnblogAuthor;
import com.ccran.entity.CnblogBlog;
import com.ccran.tools.DatabaseTools;

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
		CnblogBlog blog=resultItems.get("cnblog_blog");
		CnblogAuthor author=resultItems.get("cnblog_author");
		if(author!=null){
			DatabaseTools.InsertIntoCnblogAuthor(author);
		}
		if(blog!=null){
			DatabaseTools.InsertIntoCnblogBlog(blog);
		}
	}
}
