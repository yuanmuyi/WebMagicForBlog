package com.ccran.tools;

import java.io.FileReader;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONReader;
import com.ccran.entity.SiteFromJson;

import us.codecraft.webmagic.Site;

/**
 * 
 * @ClassName: SiteAnalysisTool
 * @Description: 使用fastjson完成对json的解析
 * @author chenran
 * @date 2018年5月6日 上午10:07:37
 * @version V1.0
 */
public class LoadSpiderTool {
	private static Logger logger=Logger.getLogger(LoadSpiderTool.class);
	/**
	 * 
	 * @Title: getSiteFromJson
	 * @Description: 通过路径解析json配置SiteFromJson
	 * @param @param
	 *            jsonPath
	 * @param @return
	 * @return SiteFromJson
	 * @version V1.0
	 */
	public static SiteFromJson getSiteFromJson(String jsonPath) {
		SiteFromJson jsonSite = null;
		try {
			// 通过fastjson反序列化SiteFromJson
			JSONReader jsonReader = new JSONReader(new FileReader(jsonPath));
			jsonReader.startArray();
			if (jsonReader.hasNext()) {
				/**
				 * 逐一读取解析 String key=jsonReader.readString(); String
				 * value=jsonReader.readString();
				 * System.out.println(key+":"+value);
				 */
				// 直接通过bean进行解析
				jsonSite = jsonReader.readObject(SiteFromJson.class);
			}
			jsonReader.endArray();
			jsonReader.close();
		} catch (Exception e) {
			System.out.println("json解析失败:" + e.getMessage());
		}
		return jsonSite;
	}

	/**
	 * 
	 * @Title: getSite
	 * @Description: 通过SiteFromJson配置Site
	 * @param @param
	 *            jsonSite
	 * @param @return
	 * @return Site
	 * @version V1.0
	 */
	public static Site getSite(SiteFromJson jsonSite) {
		Site site = null;
		if (jsonSite == null) {
			site = Site.me().setRetryTimes(3).setSleepTime(1000).setTimeOut(5000).setCharset("utf-8").setUserAgent(
					"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
			return site;
		}
		int retryTimes = jsonSite.getRetryTimes();
		int randomSleepTime = jsonSite.getRandomSleepTime();
		int timeOut = jsonSite.getTimeOut();
		String charset = jsonSite.getCharset();
		String randomUserAgent = jsonSite.getRandomUserAgent();
		logger.info("重试次数:"+retryTimes + "抓取间隔:" + randomSleepTime 
				+ "超时时间:" + timeOut + "解析编码:" + charset + "UserAgent:" + randomUserAgent);
		site = Site.me().setRetryTimes(retryTimes).setSleepTime(randomSleepTime).setTimeOut(timeOut).setCharset(charset)
				.setUserAgent(randomUserAgent);
		return site;
	}
}
