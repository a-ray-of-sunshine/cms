package org.winterframework.util;

import java.io.File;

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
}
