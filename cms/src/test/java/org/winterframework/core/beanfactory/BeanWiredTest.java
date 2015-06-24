package org.winterframework.core.beanfactory;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.winterframework.core.beanfactory.impl.BeanWired;

import com.shishuo.cms.action.ArticleAction;

public class BeanWiredTest {

	@Test
	public void testBeanWired(){
		IBeanWired beanfactory = new BeanWired();
		Map<Class<?>, Class<?>> fieldType = new HashMap<Class<?>, Class<?>>();
		
		try {
			fieldType.put(Class.forName("org.winterframework.core.beanfactory.IAnminal"), Class.forName("org.winterframework.core.beanfactory.Dog"));
			People people = beanfactory.beanWired(People.class, fieldType);
			
			System.out.println(people);
			System.out.println(people.getDog());
			System.out.println(people.getMaster());
			
			ArticleAction action = beanfactory.beanWired(ArticleAction.class, fieldType);
			System.out.println(action);
			
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
}
