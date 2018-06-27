package com.fan.e3.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Controller
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String getHtml(){
		
		return null;
	}
}
