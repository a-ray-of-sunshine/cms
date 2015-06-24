package org.winterframework.core.beanfactory;

import java.util.Map;

/**
 * 2015年6月24日11:00:19
 * 对象装配接口
 */
public interface IBeanWired {
	
	<T> T beanWired(Class<T> clazz, Map<Class<?>, Class<?>> fieldType) throws InstantiationException, IllegalAccessException;
	
}
