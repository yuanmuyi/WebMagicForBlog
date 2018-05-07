package com.ccran.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 
* @ClassName: SiteFromJson 
* @Description: site.json对应实体
* @author chenran
* @date 2018年5月6日 上午10:45:17 
* @version V1.0
 */
public class SiteFromJson{
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
}
