package org.winterframework.util;

import java.io.File;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *   2015年6月23日10:08:23
 */
public class Utils {

	public static ApplicationContext getApplicationContext(){
		return new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
	}
	
	public static String getProperyValue(String elementName, String propertyName){
		String value = "";
		SAXReader reader = new SAXReader();
		File file = new File("src/main/resources/applicationContext.xml");
		try {
			Document document = reader.read(file);
			Element root = document.getRootElement();
			
			Element node = root.element(elementName);
			value = node.attributeValue(propertyName);
			
		} catch (DocumentException e) {
			
		}
		
		return value;
	}
	
	public static String dotToSlash(String dotStr){
		String slashStr = "";
		
		slashStr = dotStr.replaceAll("\\.", "/");
		
		return slashStr;
	}
	
	public static void searchFile(File dir, List<String> fileName, String parentName){
		
		// 已经是文件了
		String name = dir.getName();
		if(dir.isFile()){
			if(name.endsWith("class")){
				name = name.substring(0, name.lastIndexOf("."));
				if(!"".equals(parentName)){
					name = parentName + "." + name;
				}
				fileName.add(name);
			}
			return;
		}
		
		File[] files = dir.listFiles();
		for(File file: files){
			String pName = file.getParentFile().getName();
			if(!"".equals(parentName)){
				pName = parentName + "." + pName;
			}
			searchFile(file, fileName, pName);
		}
		
	}
}
