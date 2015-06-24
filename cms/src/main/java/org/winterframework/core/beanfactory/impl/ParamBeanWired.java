package org.winterframework.core.beanfactory.impl;

import java.lang.reflect.Field;
import java.util.Map;

import org.winterframework.core.beanfactory.IBeanWired;

/**
 * 2015年6月24日12:12:29
 * 函数参数bean的装配，装配过程中可能有初始化参数传递进来
 * 
 */
public class ParamBeanWired extends IBeanWired {

	private Map<String, String[]> ParametersMap;
	private Class<?>[] ParametersType;
	
	protected ParamBeanWired(Map<String, String[]> parametersMap,
			Class<?>[] parametersType) {
		super();
		ParametersMap = parametersMap;
		ParametersType = parametersType;
	}

	@Override
	protected void initField(Field field, Object instance, Class<?> typeClazz)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
	}
	
	public Object[] getMethodParameters(){
		Object[] objs = null;
		
		return objs;
	}

}
