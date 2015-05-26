package com.shishuo.cms.action.manager;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("manager")
public class ManagerAction {

	@RequestMapping("index")
	public String toIndex(){
		return "index";
	}
}
