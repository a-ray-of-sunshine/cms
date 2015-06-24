package org.winterframework.core.beanfactory.impl;

import java.lang.reflect.Field;

import org.winterframework.core.beanfactory.IBeanWired;

/**
 * 2015年6月24日11:30:45
 * 对于自动装配来说，待装配类型我们是完全不可知的，
 * 所以我们只能调用默认的构造器(无参数的构造函数)来初始化实例以及实例字段
 */
public class BeanWired extends IBeanWired {

	@Override
	public void initField(Field field, Object instance, Class<?> typeClazz) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		// 设置访问级别，确保可以正常访问
		field.setAccessible(true);
		field.set(instance, typeClazz.newInstance());
	}
}
