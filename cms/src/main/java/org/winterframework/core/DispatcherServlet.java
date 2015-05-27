package org.winterframework.core;

import java.util.Map;


public class DispatcherServlet extends FrameworkServlet {

	private Map viewResolver;
	private Map mappingHandler;
	
	@Override
	protected void doInit() {
		this.getInitParameter("contextConfigLocation");
	}

	@Override
	protected void doDispatch() {

	}

}
