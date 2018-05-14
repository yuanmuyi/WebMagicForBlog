package com.ccran.processor;

import java.util.List;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

/**
 * 
* @ClassName: CSDNPageProcessor 
* @Description: 对于CSDN爬取
* @author chenran
* @date 2018年5月8日 上午11:30:26 
* @version V1.0
 */
public class CSDNPageProcessor extends BasePageProcessor {
	private static final String BLOG_PAGE_REGEX="https://blog.csdn.net/(\\w+)/article/details/(\\d+)";
	
	public CSDNPageProcessor(String path){
		super(path);
	}
	
	public void process(Page page) {
		//博客网页爬取
		if(page.getUrl().regex(BLOG_PAGE_REGEX).match()){
			crawlForBlogPage(page);
		}
	}
	
	private void crawlForBlogPage(Page page){
		//博文信息
		System.out.println("blog info:");
		String id=page.getUrl().regex(BLOG_PAGE_REGEX,2).toString();
		String title=page.getHtml().xpath("//h6[@class='title-article']/text()").toString();
		String readNum=page.getHtml().xpath("//span[@class='read-count']/text()").regex("阅读数：(\\d+)",1).toString();
		String time=page.getHtml().xpath("//span[@class='time']/text()").toString();
		time=time.replace('年', '-').replace('月','-').replace('日',' ');
		
		List<String> tagStrList=page.getHtml().xpath("//div[@class='tags-box artic-tag-box']/a[@class='tag-link']/text()").all();
		StringBuilder sb=new StringBuilder();
		for(String tagStr:tagStrList){
			sb.append(tagStr.trim()+',');
		}
		String tagListStr="";
		if(sb.length()!=0)
			tagListStr=sb.subSequence(0, sb.length()-1).toString();
		System.out.println("博客ID:"+id);
		System.out.println("博客标题:"+title);
		System.out.println("博客阅读量:"+readNum);
		System.out.println("博客发表时间:"+time);
		System.out.println("博客标签:"+tagListStr);
		//作者信息
		System.out.println("author info:");
		String author=page.getUrl().regex(BLOG_PAGE_REGEX,1).toString();
		List<Selectable> authorInfoSelList=page.getHtml().xpath("//div[@class='data-info d-flex item-tiling']/dl").nodes();
		String blogNum=authorInfoSelList.get(0).xpath("//dd/span/text()").toString();
		String fansNum=authorInfoSelList.get(1).xpath("//dd/span/text()").toString();
		String likeNum=authorInfoSelList.get(2).xpath("//dd/span/text()").toString();
		String commentNum=authorInfoSelList.get(3).xpath("//dd/span/text()").toString();
		
		authorInfoSelList=page.getHtml().xpath("//div[@class='grade-box clearfix']/dl").nodes();
		String level=authorInfoSelList.get(0).xpath("//dd/a/@title").regex("(\\d+)级",1).toString();
		String visit=authorInfoSelList.get(1).xpath("//dd/@title").toString();
		String integral=authorInfoSelList.get(2).xpath("//dd/@title").toString();
		String rank=authorInfoSelList.get(3).xpath("//dl/@title").toString();
		
		System.out.println("博主账号名:"+author);
		System.out.println("博客数量:"+blogNum);
		System.out.println("博主粉丝:"+fansNum);
		System.out.println("博主喜欢:"+likeNum);
		System.out.println("博主评论量:"+commentNum);
		System.out.println("博主等级:"+level);
		System.out.println("博主访问量:"+visit);
		System.out.println("博主积分:"+integral);
		System.out.println("博客排名:"+rank);
	}
}
