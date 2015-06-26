package org.winterframework.core;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.winterframework.core.beanfactory.IBeanWired;
import org.winterframework.core.beanfactory.impl.BeanWired;
import org.winterframework.core.beanfactory.impl.ParamBeanWired;
import org.winterframework.core.packagescanner.IPackageScanner;
import org.winterframework.core.packagescanner.impl.ScannerMappingHandler;


public class DispatcherServlet extends FrameworkServlet {

	private static final long serialVersionUID = 8675257650567541897L;
	private List<Map<String, Map<String, Object>>> mappingHandler;
	
	@Override
	protected void doInit(){
		// 0. 初始化映射处理器
		IPackageScanner scanner = new ScannerMappingHandler(this.getClass().getResource("/").getPath());
		scanner.init();
		this.mappingHandler = scanner.getHandlerMapper();
	}

	@Override
	protected void doDispatch(HttpServletRequest request,
			HttpServletResponse response) {
		String resourcePath = request.getRequestURI();
		String path =  this.handleURI(resourcePath);

		Map<String, Object> HandlerMap = this.getHandlerMap(path);
		Object result = this.dispatch(request, response, HandlerMap);
		
		String forwardPath = "/jsp/" + result + ".jsp";
		System.out.println(forwardPath);
		try {
			request.getRequestDispatcher(String.valueOf(forwardPath)).forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> getHandlerMap(String path){
		Map<String, Object> map = null;
		
		for(Map<String, Map<String, Object>> key : mappingHandler){
			if(null != key.get(path)){
				map = key.get(path);
				break;
			}
		}
		
		return map;
	}
	
	private String handleURI(String uri){
		String path = "";
		
		path = uri.substring(uri.indexOf("/", 1), uri.lastIndexOf("."));
		
		return path;
	}
	
	// 分发请求
	private Object dispatch(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> HandlerMap){
		Object obj = null;
		
		Class<?> clazz = (Class<?>) HandlerMap.get("Controller");
		Method method = (Method) HandlerMap.get("Handler");
		
		// 1. 实例化 action
		IBeanWired beanfactory = new BeanWired();
		Map<Class<?>, Class<?>> fieldType = new HashMap<Class<?>, Class<?>>();
		
		Object action = new Object();
		try {
			fieldType.put(Class.forName("org.winterframework.core.entity.IAnminal"), Class.forName("org.winterframework.core.entity.Dog"));
			action = beanfactory.beanWired(clazz, clazz.getAnnotations(), fieldType);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		// 2. 装配Method对象的参数，返回一个参数对象数组
		ParamBeanWired paramBeanWired = new ParamBeanWired(request.getParameterMap(), method);
		Object[] args = paramBeanWired.getMethodParameters(request, response);
		
		// 3. 在obj上调用method, 参数是一个对象数组
		try {
			obj = method.invoke(action, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return obj;
	}
}
