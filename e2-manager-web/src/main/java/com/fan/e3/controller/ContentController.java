package com.fan.e3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.pojo.EasyUIDataGridResult;
import com.fan.common.utils.E3Result;
import com.fan.e3.content.service.ContentService;
import com.fan.e3.pojo.TbContent;

/**
 * 查询内容
 * @TODO 
 * @author foutlook
 * @DATE 2018年2月22日
 */
@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	/**
	 * 内容查询
	 * @param categoryId
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows){
		EasyUIDataGridResult result = contentService.getContent(categoryId, page, rows);
		return result;
	}
	
	/**
	 * 内容添加
	 * @param content
	 * @return
	 */
	@RequestMapping("/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		E3Result result = contentService.addContent(content);
		return result;
	}
}
