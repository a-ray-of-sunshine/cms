package org.winterframework.core.beanfactory.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.ModelMap;
import org.winterframework.core.beanfactory.IBeanWired;

/**
 * 2015年6月24日12:12:29
 * 函数参数bean的装配，装配过程中可能有初始化参数传递进来
 * 
 */
@SuppressWarnings("unused")
public class ParamBeanWired implements IBeanWired {

	private Map<String, String[]> parametersMap;
	private Method method;
	private Map<Class<?>, Class<?>> fieldType = new HashMap<Class<?>, Class<?>>();
	
	public ParamBeanWired(Map<String, String[]> parametersMap,
			Method method) {
		super();
		this.parametersMap = parametersMap;
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
			
			// 1. 初始化默认字段
			if(isDefaultField(fieldClazz)){
				objs[i] = initDefaultField(fieldClazz, request, response);
				continue;
			}
			
			// 2. 装配其他字段
			try {
				Object obj = this.beanWired(fieldClazz, fieldAnnotation, fieldType);
				objs[i] = obj;
			} catch (Exception e){
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
		initObj(instance, clazzAnnotation);
		return instance;
	}
	
	private void initObj(Object instance, Annotation[] fieldAnnotations)
			throws IllegalArgumentException, IllegalAccessException,
			InstantiationException{
		
		Class<?> clazz = instance.getClass();
		Method[] methods = clazz.getDeclaredMethods();
		
		Map<String, String> methodMap = this.getMethodAndValue();
		for(Method method : methods){
			String name = method.getName();
			for(String methodName : methodMap.keySet()){
				if(name.equals(methodName)){
					try {
						method.invoke(instance, methodMap.get(methodName));
					} catch (InvocationTargetException e) {
						continue;
					}
				}
			}
		}
	}
	
	private boolean isDefaultField(Class<?> clazz){
		
		if(clazz.equals(HttpServletRequest.class) 
				|| clazz.equals(HttpServletResponse.class)
				|| clazz.equals(ModelMap.class)){
			return true;
		}
		
		return false;
	}
	
	private Object initDefaultField(Class<?> clazz, ServletRequest request, ServletResponse response){
		
		if(clazz.equals(HttpServletRequest.class)){
			return request;
		}else if(clazz.equals(HttpServletResponse.class)){
			return response;
		}else if(clazz.equals(ModelMap.class)){
			return new ModelMap();
		}
		
		return null;
	}
	
	private Map<String, String> getMethodAndValue(){
		Map<String, String> map = new HashMap<String, String>();
		
		for(String key : this.parametersMap.keySet()){
			String firstLetter = key.substring(0, 1);
			String subLetter = key.substring(1, key.length());
			String methodName = "set" + firstLetter.toUpperCase() + subLetter.toLowerCase();
			
			map.put(methodName, this.parametersMap.get(key)[0]);
		}
		
		return map;
	}

}
