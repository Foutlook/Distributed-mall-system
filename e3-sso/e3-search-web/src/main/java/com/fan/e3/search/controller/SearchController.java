package com.fan.e3.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fan.common.pojo.SearchResult;
import com.fan.e3.search.service.SearchService;

@Controller
public class SearchController {

	@Autowired
	private SearchService searchService;
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;

	@RequestMapping("/search")
	public String searchItemList(String keyword, @RequestParam(defaultValue = "1") Integer page, Model model)
			throws Exception {
		// 转码
		keyword = new String(keyword.getBytes("iso-8859-1"), "UTF-8");

		SearchResult searchResult = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		// 把结果传递给页面
		model.addAttribute("query", keyword);
		model.addAttribute("totalPages", searchResult.getTotalPages());
		model.addAttribute("page", page);
		model.addAttribute("recourdCount", searchResult.getRecordCount());
		model.addAttribute("itemList", searchResult.getItemList());

		// 测试异常处理器
		//int i = 1 / 0;
		
		return "search";
	}

}
