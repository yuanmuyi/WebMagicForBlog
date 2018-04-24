package com.ccran.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.ccran.entity.Author;
import com.ccran.entity.Blog;
import com.ccran.pipeline.MySQLPipeLine;
import com.ccran.tools.DatabaseTools;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.JsonPathSelector;

/**
 * 
* @ClassName: CnblogPageProcesser 
* @Description: 基于WebMagic的cnblog爬虫
* @author chenran
* @date 2018年4月7日 下午4:24:10 
* @version V1.0
 */
public class CnblogPageProcesser implements PageProcessor {
	private static final String START_URL="https://www.cnblogs.com";
	private static final String LINK_URL="(https://www.cnblogs.com/\\w+/p/\\w+.html)";
	private static final String BLOG_PAGE_URL="https://www.cnblogs.com/(\\w+)/p/(\\w+).html";
	private static final String BLOG_TITLE_XPATH="//a[@id='cb_post_title_url']/text()";
	private static final String AUTHOR_ID_REGEX="cb_blogId=(\\d+)";
	private static final String BLOG_ID_REGEX="cb_entryId=(\\d+)";
	private static final String DATE_XPATH="//div[@class='postDesc']/span[@id='post-date']/text()";
	private static final String DATE_REGEX="cb_entryCreatedDate='(\\d+/\\d+/\\d+\\s\\d+:\\d+:\\d+)'";
	private static final String READNUM_URL="https://www.cnblogs.com/mvc/blog/ViewCountCommentCout.aspx";
	private static final String READNUM_REGEX="postId=(\\w+)";
	private static final String TYPE_TAG_URL="https://www.cnblogs.com/mvc/blog/CategoriesTags.aspx";
	private static final String TYPE_TAG_REGEX="postId=(\\w+)";
	private static final String AUTHOR_INFO_URL="https://www.cnblogs.com/mvc/blog/news.aspx";
	private static final String AUTHOR_INFO_REGEX="blogApp=(\\w+)";
	private static final String AUTHOR_INFO_XPATH="//div[@id='profile_block']/a/text()";
	private static final String AUTHOR_CREATE_DATE="入园时间：(\\d+-\\d+-\\d+)";
	//重试次数、抓取间隔、编码方式
	private Site site=Site.me().setRetryTimes(3).setSleepTime(1000)
			.setCharset("utf-8").setUserAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
	//作者去重
	private Set<Integer> authorDuplicatedRemoval=new HashSet<Integer>();
	private boolean isAuthorDuplicated(int authorId){
		return !authorDuplicatedRemoval.add(authorId);
	}
	
	//抽取逻辑
	public void process(Page page) {
		//增加爬取链接到队列，默认使用HashSetDuplicatedRemoval去重
		page.addTargetRequests(page.getHtml().links().regex(LINK_URL).all());
		//博客基本信息
		if(page.getUrl().regex(BLOG_PAGE_URL).match()){
			crawlForBlog(page);
		}
		//博客阅读量
		else if(page.getUrl().regex(READNUM_URL).match()){
			crawlForReadNum(page);
		}
		//博客分类、标签
		else if(page.getUrl().regex(TYPE_TAG_URL).match()){
			crawlForBlogTypeTag(page);
		}
		//作者具体信息
		else if(page.getUrl().regex(AUTHOR_INFO_URL).match()){
			crawlForAuthor(page);
		}
		else{
			page.setSkip(true);
		}
	}
	
	/**
	 * 
	* @Title: crawlForBlog 
	* @Description: 博客基本信息
	* @param @param page
	* @return void
	* @version V1.0
	 */
	public void crawlForBlog(Page page){
		//博客页面获取信息
		String urlStr=page.getUrl().toString();
		String authorName=page.getUrl().regex(BLOG_PAGE_URL,1).toString();
		Html html=page.getHtml();
		String title=html.xpath(BLOG_TITLE_XPATH).toString();
		int authorId=Integer.parseInt(html.regex(AUTHOR_ID_REGEX, 1).toString());
		int blogId=Integer.parseInt(html.regex(BLOG_ID_REGEX, 1).toString());
		String dateStr=html.regex(DATE_REGEX,1).toString();
		//AJAX，构造URL获取：阅读量，类型标签，作者信息
		page.addTargetRequest(READNUM_URL+"?postId="+blogId);
		page.addTargetRequest(TYPE_TAG_URL+"?blogApp="+authorName+"&blogId="+authorId+"&postId="+blogId);
		//作者信息
		if(!isAuthorDuplicated(authorId)){
			Author author=new Author.Builder(authorId, Author.FLAG_PART_1).authorName(authorName)
					.url(START_URL+"/"+authorName).build();
			page.putField("author", author);
			page.addTargetRequest(AUTHOR_INFO_URL+"?blogApp="+authorName);		
		}
		//博客表信息
		Blog blog=new Blog.Builder(blogId, Blog.FLAG_PART).url(urlStr).title(title)
				.authorId(authorId).publish(dateStr).build();
		page.putField("blog", blog);
	}
	
	/**
	 * 
	* @Title: crawlForReadNum 
	* @Description: 博客阅读量
	* @param @param page
	* @return void
	* @version V1.0
	 */
	public void crawlForReadNum(Page page){
		int readNum=Integer.parseInt(page.getHtml().xpath("//body/text()").toString());
		int blogId=Integer.parseInt(page.getUrl().regex(READNUM_REGEX,1).toString());
		Blog blog=new Blog.Builder(blogId, Blog.FLAG_READ_NUM).readNum(readNum).build();
		page.putField("blog", blog);
	}
	
	/**
	 * 
	* @Title: crawlForBlogTypeTag 
	* @Description: 博客类型以及标签
	* @param @param page
	* @return void
	* @version V1.0
	 */
	public void crawlForBlogTypeTag(Page page){
		int blogId=Integer.parseInt(page.getUrl().regex(TYPE_TAG_REGEX,1).toString());
		List<String> cateList=new JsonPathSelector("$.Categories").selectList(page.getRawText());
		List<String> tagList=new JsonPathSelector("$.Tags").selectList(page.getRawText());
		cateList=Html.create(cateList.get(0)).xpath("//a/text()").all();
		tagList=Html.create(tagList.get(0)).xpath("//a/text()").all();
		/**
		 * 作为字符串插入数据库
		 */
		StringBuilder sbCateList=new StringBuilder("");
		for(int i=0;i<cateList.size();i++){
			if(i!=0)
				sbCateList.append(',');
			sbCateList.append(cateList.get(i));
		}
		StringBuilder sbTagList=new StringBuilder("");
		for(int i=0;i<tagList.size();i++){
			if(i!=0)
				sbTagList.append(',');
			sbTagList.append(tagList.get(i));
		}
		Blog blog=new Blog.Builder(blogId, Blog.FLAG_TYPE_TAG).type(sbCateList.toString())
				.tag(sbTagList.toString()).build();
		page.putField("blog", blog);
	}
	
	/**
	 * 
	* @Title: crawlForAuthor 
	* @Description: 爬取作者信息
	* @param @param page
	* @return void
	* @version V1.0
	 */
	public void crawlForAuthor(Page page){
		String authorName=page.getUrl().regex(AUTHOR_INFO_REGEX,1).toString();
		List<String> authorInfo=page.getHtml().xpath(AUTHOR_INFO_XPATH).all();
		String createDateStr=page.getHtml().regex(AUTHOR_CREATE_DATE,1).toString();
		String authorNickName=authorInfo.get(0);
		int fans=Integer.parseInt(authorInfo.get(authorInfo.size()-2));
		int attention=Integer.parseInt(authorInfo.get(authorInfo.size()-1));
		
		Author author=new Author.Builder(authorName, Author.FLAG_PART_2).authorNickName(authorNickName)
				.createDate(createDateStr).fans(fans).attention(attention).build();
		page.putField("author", author);
	}

	public Site getSite() {
		return site;
	}

	public static void main(String[] args) {
		Spider.create(new CnblogPageProcesser())
		.addUrl(START_URL).addPipeline(new ConsolePipeline()).addPipeline(new MySQLPipeLine())
		.run();
	}
}
