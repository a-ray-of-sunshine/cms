package org.winterframework.core.beanfactory.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.winterframework.core.beanfactory.IBeanWired;

/**
 * 2015年6月24日12:12:29
 * 函数参数bean的装配，装配过程中可能有初始化参数传递进来
 * 
 */
@SuppressWarnings("unused")
public class ParamBeanWired implements IBeanWired {

	private Map<String, String[]> ParametersMap;
	private Method method;
	private Map<Class<?>, Class<?>> fieldType = new HashMap<Class<?>, Class<?>>();
	
	public ParamBeanWired(Map<String, String[]> parametersMap,
			Method method) {
		super();
		this.ParametersMap = parametersMap;
		this.method = method;
	}

	public Object[] getMethodParameters(ServletRequest request, ServletResponse response){
		Class<?>[] fieldsClass = this.method.getParameterTypes();
		Annotation[][] annotations = this.method.getParameterAnnotations();

		int fieldCount = fieldsClass.length;
		Object[] objs = new Object[fieldCount];
		for(int i = 0; i < fieldCount; i++){//初始化参数
			Class<?> fieldClazz = fieldsClass[i]; 			// 字段的 Class
			Annotation[] fieldAnnotation = annotations[i];	// 字段上的 Annotation
			
			try {
				Object obj = this.beanWired(fieldClazz, fieldAnnotation, fieldType);
				objs[i] = obj;
			} catch (Exception e) {
				continue;
			}
		}
		
		return objs;
	}
	
	@Override
	public <T> T beanWired(Class<T> clazz, Annotation[] clazzAnnotation,
			Map<Class<?>, Class<?>> fieldType) throws InstantiationException,
			IllegalAccessException {
		// 1. 构建对象的实例
		T instance = clazz.newInstance();

		// 2. 对象初始化
		
		return instance;
	}
	
	private void initField(Field field, Object instance, Class<?> typeClazz, Annotation[] fieldAnnotations)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException {
		System.out.println(field);
		System.out.println(typeClazz);
		System.out.println(fieldAnnotations);
	}

}
