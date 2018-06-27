package com.fan.e3.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fan.e3.content.service.ContentService;
import com.fan.e3.pojo.TbContent;

@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	@Value("${CATAGORY_LUNBO_ID}")
	private Long CATAGORY_LUNBO_ID;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//查询内容列表
		List<TbContent> ad1List = contentService.getContentListByCId(CATAGORY_LUNBO_ID);
		//把结果返回到页面
		model.addAttribute("ad1List",ad1List);
		return "index";
	}
}
