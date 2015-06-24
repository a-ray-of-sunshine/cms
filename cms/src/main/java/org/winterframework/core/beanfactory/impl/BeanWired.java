package org.winterframework.core.beanfactory.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.winterframework.core.beanfactory.IBeanWired;

/**
 * 2015年6月24日11:30:45
 * 对于自动装配来说，待装配类型我们是完全不可知的，
 * 所以我们只能调用默认的构造器(无参数的构造函数)来初始化实例以及实例字段
 */
public class BeanWired implements IBeanWired {

	public <T> T beanWired(Class<T> clazz, Annotation[] clazzAnnotation, Map<Class<?>, Class<?>> fieldType) throws InstantiationException, IllegalAccessException{
		
		// 1. 构建对象的实例
		T instance = clazz.newInstance();
		
		// 2. 初始化对象的所有字段
		Field[] fields = clazz.getDeclaredFields();
		for(Field field: fields){
			
			// 字段类型
			Class<?> typeClazz = field.getType();
			if(typeClazz.isInterface()){
				Class<?> fieldClazz = fieldType.get(typeClazz);
				if(null != fieldClazz){
					typeClazz = fieldClazz;
				}else{// 无法装配该接口，则直接continue
					continue;
				}
			}
			
			try {
				// 如果 field 没有Autowired类型的注解，则直接抛NullPointerException
				field.getAnnotation(Autowired.class);
				
				// 3. 注入实例字段
				this.initField(field, instance, typeClazz);
			} catch (NullPointerException e) {
				// 说明该字段不需要自动装配
				continue;
			}
		}
		
		return instance;
	}

	private void initField(Field field, Object instance, Class<?> typeClazz) throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		// 设置访问级别，确保可以正常访问
		// 默认实现中直接使用默认构造函数来初始化字段，
		// 这种装配机制适用于Controller的初始化
		field.setAccessible(true);
		field.set(instance, typeClazz.newInstance());
	}
}
