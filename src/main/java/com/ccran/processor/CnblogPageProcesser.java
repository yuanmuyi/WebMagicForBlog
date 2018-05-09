package com.ccran.processor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccran.entity.CnblogAuthor;
import com.ccran.entity.CnblogBlog;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
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
public class CnblogPageProcesser extends BasePageProcessor {
	private static final String START_URL="https://www.cnblogs.com";
	
	private static final String LOW_LINK_URL="(https://www.cnblogs.com/[\\s\\S]+)";
	private static final String MID_LINK_URL="(https://www.cnblogs.com/\\w+)";
	private static final String HIGH_LINK_URL="(https://www.cnblogs.com/\\w+/p/\\w+.html)";
	
	private static final String BLOG_PAGE_URL="https://www.cnblogs.com/(\\w+)/p/(\\w+).html";
	private static final String BLOG_TITLE_XPATH="//a[@id='cb_post_title_url']/text()";
	private static final String AUTHOR_ID_REGEX="cb_blogId=(\\d+)";
	private static final String BLOG_ID_REGEX="cb_entryId=(\\d+)";
	//private static final String DATE_XPATH="//div[@class='postDesc']/span[@id='post-date']/text()";
	private static final String DATE_REGEX="cb_entryCreatedDate='(\\d+/\\d+/\\d+\\s\\d+:\\d+:\\d+)'";
	private static final String READNUM_URL="https://www.cnblogs.com/mvc/blog/ViewCountCommentCout.aspx";
	private static final String READNUM_REGEX="postId=(\\w+)";
	private static final String TYPE_TAG_URL="https://www.cnblogs.com/mvc/blog/CategoriesTags.aspx";
	private static final String TYPE_TAG_REGEX="postId=(\\w+)";
	private static final String AUTHOR_INFO_URL="https://www.cnblogs.com/mvc/blog/news.aspx";
	private static final String AUTHOR_INFO_REGEX="blogApp=(\\w+)";
	private static final String AUTHOR_INFO_XPATH="//div[@id='profile_block']/a/text()";
	private static final String AUTHOR_CREATE_DATE="入园时间：(\\d+-\\d+-\\d+)";

	public CnblogPageProcesser(String path){
		super(path);
	}
	
	//作者去重，同时进行博主账号名-博主ID的映射
	private Map<String,Integer> authorDuplicatedRemoval=new HashMap<String,Integer>();
	private boolean isAuthorDuplicated(String authorName,int authorId){
		boolean res=authorDuplicatedRemoval.containsKey(authorName);
		authorDuplicatedRemoval.put(authorName, authorId);
		return res;
	}
	private int getAuthorIdByName(String authorName){
		return authorDuplicatedRemoval.get(authorName);
	}
	
	//抽取逻辑
	public void process(Page page) {
		//增加爬取链接到队列，默认使用HashSetDuplicatedRemoval去重
		page.addTargetRequests(page.getHtml().links().regex(HIGH_LINK_URL).all(),3);
		page.addTargetRequests(page.getHtml().links().regex(MID_LINK_URL).all(),2);
		page.addTargetRequests(page.getHtml().links().regex(LOW_LINK_URL).all(),1);
		//for(String url:page.getHtml().links().regex(LINK_URL).all())
		//	System.out.println(url);
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
		//不进行pipeline的处理
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
		page.addTargetRequest(new Request(READNUM_URL+"?postId="+blogId).setPriority(5));
		page.addTargetRequest(new Request(TYPE_TAG_URL+"?blogApp="+authorName+"&blogId="+authorId+"&postId="+blogId).setPriority(5));
		//作者信息封装成对象交由pipeline处理
		if(!isAuthorDuplicated(authorName,authorId)){
			CnblogAuthor author=new CnblogAuthor.Builder(authorId, CnblogAuthor.FLAG_PART_1).authorName(authorName)
					.url(START_URL+"/"+authorName).build();
			page.putField("cnblog_author", author);
			page.addTargetRequest(new Request(AUTHOR_INFO_URL+"?blogApp="+authorName).setPriority(10));		
		}
		//博客信息封装成对象交由pipeline处理
		CnblogBlog blog=new CnblogBlog.Builder(blogId, CnblogBlog.FLAG_PART).url(urlStr).title(title)
				.authorId(authorId).publish(dateStr).build();
		page.putField("cnblog_blog", blog);
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
		//xpath、regex提取阅读量
		int readNum=Integer.parseInt(page.getHtml().xpath("//body/text()").toString());
		int blogId=Integer.parseInt(page.getUrl().regex(READNUM_REGEX,1).toString());
		//封装成对象交由pipeline处理
		CnblogBlog blog=new CnblogBlog.Builder(blogId, CnblogBlog.FLAG_READ_NUM).readNum(readNum).build();
		page.putField("cnblog_blog", blog);
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
		//提取Json信息
		int blogId=Integer.parseInt(page.getUrl().regex(TYPE_TAG_REGEX,1).toString());
		List<String> cateList=new JsonPathSelector("$.Categories").selectList(page.getRawText());
		List<String> tagList=new JsonPathSelector("$.Tags").selectList(page.getRawText());
		cateList=Html.create(cateList.get(0)).xpath("//a/text()").all();
		tagList=Html.create(tagList.get(0)).xpath("//a/text()").all();
		//类别、标签组合成串
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
		//封装成对象交由pipeline处理
		CnblogBlog blog=new CnblogBlog.Builder(blogId, CnblogBlog.FLAG_TYPE_TAG).type(sbCateList.toString())
				.tag(sbTagList.toString()).build();
		page.putField("cnblog_blog", blog);
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
		//xpath、regex获取作者基本信息
		String authorName=page.getUrl().regex(AUTHOR_INFO_REGEX,1).toString();
		//通过Map获取ID
		int authorId=getAuthorIdByName(authorName);
		//内容解析
		List<String> authorInfo=page.getHtml().xpath(AUTHOR_INFO_XPATH).all();
		String createDateStr=page.getHtml().regex(AUTHOR_CREATE_DATE,1).toString();
		String authorNickName=authorInfo.get(0);
		int fans=Integer.parseInt(authorInfo.get(authorInfo.size()-2));
		int attention=Integer.parseInt(authorInfo.get(authorInfo.size()-1));
		//封装成对象交由pipeline处理
		CnblogAuthor author=new CnblogAuthor.Builder(authorId, CnblogAuthor.FLAG_PART_2).authorName(authorName)
				.authorNickName(authorNickName).createDate(createDateStr)
				.fans(fans).attention(attention).build();
		page.putField("cnblog_author", author);
	}
}
