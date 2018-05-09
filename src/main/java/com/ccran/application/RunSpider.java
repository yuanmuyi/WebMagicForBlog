package com.ccran.application;

import com.ccran.pipeline.MySQLPipeLine;
import com.ccran.processor.CnblogPageProcesser;
import com.ccran.processor.IPProxyProcessor;
import com.ccran.tools.LoadJsonTool;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

public class RunSpider {
	private static final String CNBLOGS_START_URL="https://www.cnblogs.com";
	private static final String XICI_IP_PROXY_URL="http://www.xicidaili.com/";
	
	public static void main(String[] args) {
		//RunCnblogSpider();
		RunIPProxySpider();
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
		/**
		 * 使用Processor为CnblogPageProcesser
		 * Pipeline包括ConsolePipeline以及MySQLPipeLine
		 * Scheduler使用PriorityScheduler进行基于优先级的调度
		 * 初始URL为博客园主页
		 */
		Spider.create(new CnblogPageProcesser("site.json")).setScheduler(new PriorityScheduler())
		.addUrl(CNBLOGS_START_URL).addPipeline(new ConsolePipeline()).addPipeline(new MySQLPipeLine())
		.run();
	}
	
	/**
	 * 
	* @Title: RunIPProxySpider 
	* @Description: 对免费代理IP网站爬取
	* @param 
	* @return void
	* @version V1.0
	 */
	public static void RunIPProxySpider(){
		Spider.create(new IPProxyProcessor("site.json")).addUrl(XICI_IP_PROXY_URL)
		.addPipeline(new ConsolePipeline()).run();
		System.out.println("hello world");
	}
}
