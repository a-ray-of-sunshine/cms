package org.winterframework.core.beanfactory;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 2015年6月24日11:00:19
 * 对象装配接口
 */
public abstract class IBeanWired {
	
	/**
	 * 对象装配，两个步骤：
	 * ① 初始化对象的实例
	 * ② 初始化对象的实例字段（类的静态字段在类被装载的时候就被初始化好了）
	 */
	public <T> T beanWired(Class<T> clazz, Map<Class<?>, Class<?>> fieldType) throws InstantiationException, IllegalAccessException{
		
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
	
	protected abstract void initField(Field field, Object instance, Class<?> typeClazz) throws IllegalArgumentException, IllegalAccessException, InstantiationException;
}
