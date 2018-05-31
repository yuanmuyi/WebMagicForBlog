package com.ccran.application;

import java.util.ArrayList;
import java.util.List;

import com.ccran.entity.IPProxyItem;
import com.ccran.pipeline.MySQLPipeLine;
import com.ccran.processor.CSDNPageProcessor;
import com.ccran.processor.CnblogPageProcesser;
import com.ccran.processor.IPProxyProcessor;
import com.ccran.tools.DatabaseTool;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.PriorityScheduler;

public class RunSpider {
	private static final String CNBLOGS_START_URL = "https://www.cnblogs.com";
	private static final String CSDN_START_URL = "https://blog.csdn.net";
	private static final String XICI_IP_PROXY_URL = "http://www.xicidaili.com/";
	private static final String SITE_JSON_PATH = "site.json";

	public static void main(String[] args) {
		//RunCnblogSpider();
		//RunIPProxySpider();
		RunCSDNSpider();
	}

	/**
	 * 
	 * @Title: RunCnblogSpider
	 * @Description: 运行cnblog爬虫
	 * @param
	 * @return void
	 * @version V1.0
	 */
	public static void RunCnblogSpider() {
		/**
		 * 使用Processor为CnblogPageProcesser
		 * Pipeline包括ConsolePipeline以及MySQLPipeLine
		 * Scheduler使用PriorityScheduler进行基于优先级的调度 初始URL为博客园主页
		 */
		Spider.create(new CnblogPageProcesser(SITE_JSON_PATH)).
		setScheduler(new PriorityScheduler()).addUrl(CNBLOGS_START_URL)
		.addPipeline(new ConsolePipeline()).addPipeline(new MySQLPipeLine()).run();
	}

	/**
	 * 
	 * @Title: RunIPProxySpider
	 * @Description: 对免费代理IP网站爬取
	 * @param
	 * @return void
	 * @version V1.0
	 */
	public static void RunIPProxySpider() {
		Spider.create(new IPProxyProcessor(SITE_JSON_PATH))
		.addUrl(XICI_IP_PROXY_URL).addPipeline(new ConsolePipeline())
		.run();
	}

	public static void RunCSDNSpider() {
		while (true) {
			//代理爬取
			DatabaseTool.clearIPProxyTable();
			Spider.create(new IPProxyProcessor(SITE_JSON_PATH))
			.addUrl(XICI_IP_PROXY_URL).run();
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println(e);
			}
			// 非代理运行3min
			Spider spider = Spider.create(new CSDNPageProcessor(SITE_JSON_PATH))
					.setScheduler(new FileCacheQueueScheduler("cache")).addUrl(CSDN_START_URL)
					.addPipeline(new ConsolePipeline()).addPipeline(new MySQLPipeLine()).thread(3);
			spider.start();
			try {
				Thread.sleep(3 * 60 * 1000);
			} catch (Exception e) {
				System.out.println(e);
			}
			spider.stop();
			// 运行代理爬虫5min
			List<IPProxyItem> items = DatabaseTool.getIPProxyItemList();
			List<Proxy> proxyList = new ArrayList<Proxy>();
			for (IPProxyItem item : items) {
				if (item.getType().equals("HTTPS") || item.getType().equals("HTTP")) {
					proxyList.add(new Proxy(item.getIpAddress(), item.getPort()));
				}
			}
			HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
			httpClientDownloader.setProxyProvider(new SimpleProxyProvider(proxyList));
			// CSDN爬虫
			spider = Spider.create(new CSDNPageProcessor(SITE_JSON_PATH))
					.setScheduler(new FileCacheQueueScheduler("cache")).addUrl(CSDN_START_URL)
					.addPipeline(new ConsolePipeline()).addPipeline(new MySQLPipeLine())
					.setDownloader(httpClientDownloader).thread(3);
			spider.start();
			try {
				Thread.sleep(5 * 60 * 1000);
			} catch (Exception e) {
				System.out.println(e);
			}
			spider.stop();
		}
	}
}
