package com.shishuo.cms.action.manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.winterframework.core.entity.Dog;

import com.shishuo.cms.dao.AdminDao;
import com.shishuo.cms.entity.Admin;
import com.shishuo.cms.entity.vo.PageVo;

@Controller
@RequestMapping("manager")
public class ManagerAction {

	@Autowired
	private AdminDao adminDao;
	
	@RequestMapping("index")
	public String toIndex(ModelMap modelMap, 
			HttpServletRequest request, HttpServletResponse response, Dog dog, String hello){
		// http://localhost:8080/cms/manager/index.do?hello=world&dogName=xiaoxiao&age=12&weight=43.1
		System.out.println(dog.getDogName());
		System.out.println(dog.getAge());
		System.out.println(dog.getWeight());
		System.out.println(hello);
		return "index";
	}
	
	@RequestMapping("getUserList")
	public void getUserList(HttpServletResponse response){
		List<Admin> list = adminDao.getAllList(0, 10);
		
		PageVo<Admin> pageVo = new PageVo<Admin>(10);
		pageVo.setList(list);
		pageVo.setCount(list.size());
		
		try {
			PrintWriter out = response.getWriter();
			out.print(list);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
