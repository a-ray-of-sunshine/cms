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
			IllegalAccessException{
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
						// 这里在进行方法调用的时候要注意，将参数的类型进行正确的转换，否则这个invoke方法就调用失败
						// 例如int类型的参数，就要进行转换。
						Object param = methodMap.get(methodName);
						try {
							param = getParam(method, param);
						} catch (NoSuchMethodException e) {
							param = methodMap.get(methodName);
						} catch (SecurityException e) {
							param = methodMap.get(methodName);
						}
						method.invoke(instance, param);
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
			String methodName = "set" + firstLetter.toUpperCase() + subLetter;
			
			map.put(methodName, this.parametersMap.get(key)[0]);
		}
		
		return map;
	}
	
	
	private static Object getParam(Method method, Object param) throws 
			NoSuchMethodException, SecurityException, 
			IllegalAccessException, IllegalArgumentException, 
			InvocationTargetException{
		Object obj = param;
		
		Class<?>[] clazzs = method.getParameterTypes();
		Class<?>  paramClass; 
		if(null != clazzs & clazzs.length > 0){
			paramClass = clazzs[0];
			// 判断参数是否是原生数据类
			if(null != (paramClass = getParamClass(paramClass))){
				Method valMethod = paramClass.getDeclaredMethod("valueOf", String.class);
				obj = valMethod.invoke(null, param.toString());
			}
		}
		
		return obj;
	}
	
	private static Class<?> getParamClass(Class<?> paramClass){
		Map<String, Class<?>> map = new HashMap<String, Class<?>>();
		
		map.put("int", Integer.class);
		map.put("boolean", Boolean.class);
		map.put("byte", Byte.class);
		map.put("char", Character.class);
		map.put("short", Short.class);
		map.put("float", Float.class);
		map.put("double", Double.class);
		map.put("long", Long.class);
		
		return map.get(paramClass.getName());
	}

}
