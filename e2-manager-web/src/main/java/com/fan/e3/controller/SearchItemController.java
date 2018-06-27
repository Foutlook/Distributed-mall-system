package com.fan.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.utils.E3Result;
import com.fan.e3.search.service.SearchItemService;

/**
 * 导入索引库
 * @TODO 
 * @author foutlook
 * @DATE 2018年2月24日
 */
@Controller
public class SearchItemController {
	
	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result getSearchItem(){
		E3Result inportAllItems = searchItemService.inportAllItems();
		return inportAllItems;
	}
}
