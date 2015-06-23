package org.winterframework.core;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println(this.mappingHandler);
	}

}
