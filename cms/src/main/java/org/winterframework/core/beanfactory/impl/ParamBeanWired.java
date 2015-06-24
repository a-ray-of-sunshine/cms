package org.winterframework.core.beanfactory.impl;

import java.lang.reflect.Field;

import org.winterframework.core.beanfactory.IBeanWired;

/**
 * 2015年6月24日12:12:29
 * 函数参数bean的装配，装配过程中可能有初始化参数传递进来
 * 
 */
public class ParamBeanWired extends IBeanWired {

	@Override
	protected void initField(Field field, Object instance, Class<?> typeClazz)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
	}

}
