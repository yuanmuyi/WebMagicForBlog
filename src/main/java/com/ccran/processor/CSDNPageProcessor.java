package com.ccran.processor;

import java.util.List;

import com.ccran.entity.CSDNAuthor;
import com.ccran.entity.CSDNBlog;
import com.ccran.tools.DatabaseTool;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Spider;
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
	private static final String BLOG_PAGE_REGEX = "([\\s\\S]+blog.csdn.net/(\\w+)/article/details/(\\d+))";
	private static final String BLOG_LINKS="([\\s\\S]+blog.csdn.net[\\s\\S]+)";
	
	public CSDNPageProcessor(String path) {
		super(path);
	}

	public void process(Page page) {
		// 其他内链的添加
		page.addTargetRequests(page.getHtml().links().regex(BLOG_LINKS).all());
		//System.out.println(page.getHtml().links().regex(BLOG_LINKS).all());
		// 博客网页爬取
		if (page.getUrl().regex(BLOG_PAGE_REGEX).match()) {
			crawlForBlogPage(page);
		}
	}

	private void crawlForBlogPage(Page page) {
		// 作者信息
		String authorName = page.getUrl().regex(BLOG_PAGE_REGEX, 2).toString();
		List<Selectable> authorInfoSelList = page.getHtml().xpath("//div[@class='data-info d-flex item-tiling']/dl")
				.nodes();
		int blogNum = Integer.parseInt(authorInfoSelList.get(0).xpath("//dd/span/text()").toString());
		int fansNum = Integer.parseInt(authorInfoSelList.get(1).xpath("//dd/span/text()").toString());
		int likeNum = Integer.parseInt(authorInfoSelList.get(2).xpath("//dd/span/text()").toString());
		int commentNum = Integer.parseInt(authorInfoSelList.get(3).xpath("//dd/span/text()").toString());

		authorInfoSelList = page.getHtml().xpath("//div[@class='grade-box clearfix']/dl").nodes();
		int level = Integer.parseInt(authorInfoSelList.get(0).xpath("//dd/a/@title").regex("(\\d+)级", 1).toString());
		int visitNum = Integer.parseInt(authorInfoSelList.get(1).xpath("//dd/@title").toString());
		int integral = Integer.parseInt(authorInfoSelList.get(2).xpath("//dd/@title").toString());
		int rank = Integer.parseInt(authorInfoSelList.get(3).xpath("//dl/@title").toString());
		// 封装后交由pipeline处理
		CSDNAuthor author = new CSDNAuthor(authorName, blogNum, fansNum, likeNum, commentNum, level, visitNum, integral,
				rank);
		page.putField("csdn_author", author);
		// 博文信息
		int blogId = Integer.parseInt(page.getUrl().regex(BLOG_PAGE_REGEX, 3).toString());
		String title = page.getHtml().xpath("//h6[@class='title-article']/text()").toString();
		int readNum = Integer
				.parseInt(page.getHtml().xpath("//span[@class='read-count']/text()").regex("阅读数：(\\d+)", 1).toString());
		String publishTime = page.getHtml().xpath("//span[@class='time']/text()").toString();
		publishTime = publishTime.replace('年', '-').replace('月', '-').replace('日', ' ');
		List<String> tagStrList = page.getHtml()
				.xpath("//div[@class='tags-box artic-tag-box']/a[@class='tag-link']/text()").all();
		StringBuilder sb = new StringBuilder();
		for (String tagStr : tagStrList) {
			sb.append(tagStr.trim() + ',');
		}
		String tag = "";
		if (sb.length() != 0)
			tag = sb.subSequence(0, sb.length() - 1).toString();
		CSDNBlog blog = new CSDNBlog(blogId, title, readNum, publishTime, tag, 0);
		page.putField("csdn_blog", blog);
	}
	
	public static void main(String[] args) {
		Spider.create(new CSDNPageProcessor("site.json")).addUrl("https://blog.csdn.net/").start();
	}
}
