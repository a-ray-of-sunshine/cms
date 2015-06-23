package org.winterframework.core.packagescanner.impl;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.winterframework.core.packagescanner.IPackageScanner;
import org.winterframework.util.Utils;

public class ScannerMappingHandler implements IPackageScanner {
	
	private List<Map<String, Map<String, Object>>> handlerMapper = new ArrayList<Map<String, Map<String, Object>>>();
	
	private String packagePath;
	
	public ScannerMappingHandler(String packagePath) {
		super();
		this.packagePath = packagePath;
	}

	public List<Map<String, Map<String, Object>>> getHandlerMapper() {
		return handlerMapper;
	}

	@Override
	public void init(){
		List<String> nameList = this.getClassFileNameList();
		
		try {
			initHandlerMapper(nameList);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 扫描配置的包路径，获得类名的List
	 * @return
	 */
	private List<String> getClassFileNameList(){
		File file = new File(this.packagePath + "/applicationContext.xml");
		String pack = Utils.getProperyValue(file, "component-scan", "base-package");
		String packageName = Utils.dotToSlash(pack);
//		String classPath = this.getClass().getResource("").getPath();
//		String classPath = "/E:/develop/java/cms/target/classes/";
		String classPath = this.packagePath;
		
		String scannerPath = classPath + packageName;
		scannerPath = scannerPath.substring(1, scannerPath.length());
		
		File classDir = new File(scannerPath);
		List<String> classFileList = new ArrayList<String>();
		Utils.searchFile(classDir, classFileList, "");
		
		String packagePrefix = pack.substring(0, pack.lastIndexOf("."));
		List<String> resList = new ArrayList<String>();
		for(String name : classFileList){
			resList.add(packagePrefix + "." + name);
		}
		
		return resList;
	}
	
	/**
	 * 初始化映射处理器
	 * @param nameList
	 * @throws ClassNotFoundException 
	 */
	private void initHandlerMapper(List<String> nameList) throws ClassNotFoundException{
		for(String className : nameList){
			Class<?> clazz = Class.forName(className);
			Controller ctrl = clazz.getAnnotation(Controller.class);
			
			// 说明该类是control
			if(null != ctrl){
				RequestMapping ann = clazz.getAnnotation(RequestMapping.class);
				String classMapPath = "";
				if(null != ann){
					classMapPath = ann.value()[0];
				}
				
				Method[] methods = clazz.getDeclaredMethods();
				for(Method method : methods){
					RequestMapping methodAnn = method.getAnnotation(RequestMapping.class);
					if(null != methodAnn){
						Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
						Map<String, Object> valueMap = new HashMap<String, Object>();
						
						String path = this.getFullPath(classMapPath, methodAnn.value()[0]);
						valueMap.put("Controller", clazz);
						valueMap.put("Handler", method);
						map.put(path, valueMap);
						
						handlerMapper.add(map);
						System.out.println(path);
						System.out.println("\tController:\t" + clazz);
						System.out.println("\tHandler:\t" + method);
					}
				}
			}

		}
	}
	
	private String getFullPath(String classMapPath, String path){
		String res = "";
		
		if(!classMapPath.startsWith("/")){
			classMapPath = "/" + classMapPath;
		}
		
		if(classMapPath.endsWith("/") && path.startsWith("/")){
			res = classMapPath + path.substring(1, path.length());
		}else if(classMapPath.endsWith("/") || path.startsWith("/")){
			res = classMapPath + path;
		}else{
			res = classMapPath + "/" + path;
		}
		
		return res;
	}
}
