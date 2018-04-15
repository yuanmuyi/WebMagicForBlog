package com.ccran.LearnWebMagic;

import us.codecraft.webmagic.selector.Html;

public class Test {
	public static void main(String[] args) {
		Html html=new Html("cb_entryCreatedDate='2018/4/14 16:15:00';");
		System.out.println(html.regex("cb_entryCreatedDate='(\\d+/\\d+/\\d+\\s\\d+:\\d+:\\d+)'",1).toString());
		System.out.println("https:www.cnblog.com"+"/"+"cran");
	}
}
