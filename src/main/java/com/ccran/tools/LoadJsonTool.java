package com.ccran.tools;

import java.io.File;
import java.io.FileReader;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONReader;
import com.ccran.entity.SiteFromJson;

import us.codecraft.webmagic.Site;

/**
 * 
 * @ClassName: LoadJsonTool
 * @Description: 使用fastjson完成对json的解析
 * @author chenran
 * @date 2018年5月6日 上午10:07:37
 * @version V1.0
 */
public class LoadJsonTool {
	public static SiteFromJson jsonSite=null;
	
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
		try {
			//对site.json文件获取
			File file=new File("src/main/conf/"+jsonPath);
			if(!file.exists())
				file=new File(jsonPath);
			FileReader fileReader=new FileReader(file);
			// 通过fastjson反序列化SiteFromJson
			JSONReader jsonReader = new JSONReader(fileReader);
			jsonReader.startArray();
			if (jsonReader.hasNext()) {
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
}
