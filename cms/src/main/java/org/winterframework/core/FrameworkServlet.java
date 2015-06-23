package org.winterframework.core;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class FrameworkServlet extends HttpServlet {

	private static final long serialVersionUID = -4663789872003440555L;

	@Override
	public void init() throws ServletException {
		super.init();
		doInit();
	}
	
	@Override
	protected void service(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		this.doDispatch(arg0, arg1);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doDispatch(req, resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doDispatch(req, resp);
	}
	
	/**
	 * 初始化 映射处理器 和 视图分解器
	 */
	protected abstract void doInit();
	
	/**
	 * 分发请求，处理请求，并返回视图
	 */
	protected abstract void doDispatch(HttpServletRequest request, HttpServletResponse response);
}
