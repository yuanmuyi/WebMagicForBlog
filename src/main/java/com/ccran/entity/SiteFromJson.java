package com.ccran.entity;

import java.util.List;

import org.apache.log4j.Logger;
import us.codecraft.webmagic.Site;

/**
 * 
* @ClassName: SiteFromJson 
* @Description: site.json对应实体
* @author chenran
* @date 2018年5月6日 上午10:45:17 
* @version V1.0
 */
public class SiteFromJson{
	private static Logger logger=Logger.getLogger(SiteFromJson.class);
	
	private int retryTimes;
	private int minSleepTime;
	private int maxSleepTime;
	private int timeOut;
	private String charset;
	private List<String> userAgent;
	public int getRetryTimes() {
		return retryTimes;
	}
	public void setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
	}
	public int getMinSleepTime() {
		return minSleepTime;
	}
	public void setMinSleepTime(int minSleepTime) {
		this.minSleepTime = minSleepTime;
	}
	public int getMaxSleepTime() {
		return maxSleepTime;
	}
	public void setMaxSleepTime(int maxSleepTime) {
		this.maxSleepTime = maxSleepTime;
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public List<String> getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(List<String> userAgent) {
		this.userAgent = userAgent;
	}
	
	/**
	 * 
	* @Title: getRandomSleepTime 
	* @Description: 获取最小最大时间之间的随机时间
	* @param @return
	* @return int
	* @version V1.0
	 */
	public int getRandomSleepTime(){
		return (int) (minSleepTime+Math.random()*maxSleepTime);
	}
	
	/**
	 * 
	* @Title: getRandomUserAgent 
	* @Description: 返回UserAgent列表中的一个随机项
	* @param @return
	* @return String
	* @version V1.0
	 */
	public String getRandomUserAgent(){
		int randomIndex=(int) (Math.random()*userAgent.size());
		return userAgent.get(randomIndex);
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
