package com.ccran.processor;

import com.ccran.entity.SiteFromJson;
import com.ccran.tools.LoadJsonTool;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 
* @ClassName: BasePageProcessor 
* @Description: 完成对Site的基本设置
* @author chenran
* @date 2018年5月8日 上午10:36:12 
* @version V1.0
 */
public abstract class BasePageProcessor implements PageProcessor {
	public BasePageProcessor(){}
	
	/**
	 * 
	* @Title:  
	* @Description: 通过json文件加载Site  
	* @param @param jsonPath
	 */
	public BasePageProcessor(String jsonPath){
		if(LoadJsonTool.jsonSite==null){
			LoadJsonTool.getSiteFromJson(jsonPath);
		}
		site=SiteFromJson.getSite(LoadJsonTool.jsonSite);
	}
	
	//Site完成对爬取目标的基本配置，包括抓取间隔、重试次数、最大延迟等信息
	private Site site = null;

	public void setSite(Site site){
		this.site=site;
	}
	
	public Site getSite(){
		return site;
	}
}
