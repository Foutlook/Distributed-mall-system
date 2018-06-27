package com.fan.e3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @TODO  页面跳转
 * @author foutlook
 * @DATE 2018年2月3日
 */
@Controller
public class PageController {
	
	@RequestMapping("/")
	public String showIndex(){
		
		return "index";
	}
	
	@RequestMapping("/{page}")
	public String showPage(@PathVariable String page){
		
		return page;
	}
}
