package com.ccran.processor;

import java.util.ArrayList;
import java.util.List;

import com.ccran.entity.IPProxyItem;
import com.ccran.tools.DatabaseTool;
import com.ccran.tools.LoadJsonTool;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 
* @ClassName: IPProxyProcessor 
* @Description: IP代理获取,爬取网站为西刺免费代理
* @author chenran
* @date 2018年5月7日 下午1:34:01 
* @version V1.0
 */
public class IPProxyProcessor extends BasePageProcessor {

	public IPProxyProcessor(String path){
		super(path);
	}
	
	public void process(Page page) {
		//进行信息提取
		List<Selectable> sel=page.getHtml()
				.xpath("//table[@id='ip_list']/tbody/tr[@class]").nodes();
		for(Selectable s:sel){
			//获取每一项IP信息
			List<String> items=s.xpath("//td/text()").all();
			if(items.size()==0)
				continue;
			String ipAddress=items.get(1);
			int port=Integer.parseInt(items.get(2));
			String serverLocate=items.get(3);
			String strAnonymity=items.get(4);
			int anonymity=IPProxyItem.HIGH_ANONYMITY;
			if(strAnonymity.equals("透明"))
				anonymity=IPProxyItem.CLARITY;
			String type=items.get(5);
			//封装成IPProxyItem，并且插入数据表
			IPProxyItem item=new IPProxyItem(ipAddress, port, serverLocate, 
					anonymity, type);
			DatabaseTool.InsertIntoIPProxy(item);
		}
	}
}
