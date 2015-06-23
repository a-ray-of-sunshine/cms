package org.winterframework.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

public class UtilTest {

	@Test
	public void testContext() throws IOException{
		ApplicationContext ac = Utils.getApplicationContext();
		System.out.println(ac);
	}
	
	@Test
	public void testParseXML(){
	 assertEquals(Utils.getProperyValue("component-scan", "base-package"), "com.shishuo.cms");
	}
}
