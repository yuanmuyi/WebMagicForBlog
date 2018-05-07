package com.ccran.application;

import com.ccran.pipeline.MySQLPipeLine;
import com.ccran.processor.CnblogPageProcesser;
import com.ccran.tools.LoadSpiderTool;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

public class RunSpider {
	private static final String CNBLOGS_START_URL="https://www.cnblogs.com";
	
	public static void main(String[] args) {
		RunCnblogSpider();
	}

	/**
	 * 
	* @Title: RunCnblogSpider 
	* @Description: 运行cnblog爬虫
	* @param 
	* @return void
	* @version V1.0
	 */
	public static void RunCnblogSpider(){
		//通过读取json配置Site,同时生成Processor
		Site site=LoadSpiderTool.getSite(LoadSpiderTool.getSiteFromJson("site.json"));
		CnblogPageProcesser processor=new CnblogPageProcesser();
		processor.setSite(site);
		/**
		 * 使用Processor为CnblogPageProcesser
		 * Pipeline包括ConsolePipeline以及MySQLPipeLine
		 * Scheduler使用PriorityScheduler进行基于优先级的调度
		 * 初始URL为博客园主页
		 */
		Spider.create(processor).setScheduler(new PriorityScheduler())
		.addUrl(CNBLOGS_START_URL).addPipeline(new ConsolePipeline()).addPipeline(new MySQLPipeLine())
		.run();
	}
}
