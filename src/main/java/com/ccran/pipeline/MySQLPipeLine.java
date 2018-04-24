package com.ccran.pipeline;

import com.ccran.entity.Author;
import com.ccran.entity.Blog;
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
		Blog blog=resultItems.get("blog");
		Author author=resultItems.get("author");
		if(author!=null)
			DatabaseTools.InsertIntoAuthor(author);
		if(blog!=null)
			DatabaseTools.InsertIntoBlog(blog);
	}
}
