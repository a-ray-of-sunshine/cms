package org.winterframework.core.beanfactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.ui.ModelMap;
import org.winterframework.core.beanfactory.impl.BeanWired;
import org.winterframework.core.beanfactory.impl.ParamBeanWired;

import com.shishuo.cms.action.AuthAction;

public class ParamBeanWiredTest {

	@Test
	public void testParamBeanWired(){
		// 1. 使用 BeanWired类 构建action的实例  obj
		Class<?> clazz = AuthAction.class;
		
		IBeanWired beanfactory = new BeanWired();
		Map<Class<?>, Class<?>> fieldType = new HashMap<Class<?>, Class<?>>();
		Object action = new Object();
		try {
			action = beanfactory.beanWired(clazz, clazz.getAnnotations(), fieldType);
			System.out.println(action);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		// 2. 装配Method对象的参数，返回一个参数对象数组
		// ParametersMap 初始化参数。
		// Method对象的参数。ParametersType
		Method method = null;
		try {
			method = clazz.getDeclaredMethod("adminLogin", new Class<?>[]{HttpServletRequest.class, ModelMap.class, Dog.class});
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		System.out.println(method);
		ParamBeanWired paramBeanWired = new ParamBeanWired(null, method);
		Object[] args = paramBeanWired.getMethodParameters(null, null);
		
		// 3. 在obj上调用method, 参数是一个对象数组
		// Method.invoke(obj, args);
		try {
			Object res = method.invoke(action, args);
			System.out.println(res);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
