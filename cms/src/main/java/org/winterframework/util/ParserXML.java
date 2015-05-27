package org.winterframework.util;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;

import sun.misc.Resource;

/**
 * 
 * @author blaze
 *
 */
public class ParserXML {

	private static String xmlPath;
	
	public ParserXML getParser(String xmlPath){
		this.xmlPath = xmlPath;
		
		ClassPathResource resource = new ClassPathResource(this.xmlPath, ApplicationContext.class);
		
		return this;
	}
	
	
	
}
