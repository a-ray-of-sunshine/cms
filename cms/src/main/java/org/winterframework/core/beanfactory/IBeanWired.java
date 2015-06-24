package org.winterframework.core.beanfactory;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 2015年6月24日11:00:19
 * 对象装配接口
 */
public interface IBeanWired {
	
	/**
	 * 对象装配，两个步骤：
	 * ① 初始化对象的实例
	 * ② 初始化对象的实例字段（类的静态字段在类被装载的时候就被初始化好了）
	 */
	public <T> T beanWired(Class<T> clazz, Annotation[] clazzAnnotation, Map<Class<?>, Class<?>> fieldType) throws InstantiationException, IllegalAccessException;
}
