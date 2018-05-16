package com.ccran.entity;

/**
 * 
* @ClassName: IPProxyItem 
* @Description: 对应代理IP的某项
* @author chenran
* @date 2018年5月7日 下午1:46:18 
* @version V1.0
 */
public class IPProxyItem {
	public static final int HIGH_ANONYMITY=1;
	public static final int CLARITY=2;
	//ip地址
	private String ipAddress;
	//端口
	private int port;
	//服务器位置
	private String serverLocate;
	//匿名性
	private int anonymity;
	//类型
	private String type;
	
	public IPProxyItem(){}
	
	public IPProxyItem(String ipAddress, int port, String serverLocate, int anonymity, String type) {
		super();
		this.ipAddress = ipAddress;
		this.port = port;
		this.serverLocate = serverLocate;
		this.anonymity = anonymity;
		this.type = type;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getServerLocate() {
		return serverLocate;
	}
	public void setServerLocate(String serverLocate) {
		this.serverLocate = serverLocate;
	}
	public int getAnonymity() {
		return anonymity;
	}
	public void setAnonymity(int anonymity) {
		this.anonymity = anonymity;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return ipAddress+":"+port+":"+serverLocate+":"+type;
	}
	
}
