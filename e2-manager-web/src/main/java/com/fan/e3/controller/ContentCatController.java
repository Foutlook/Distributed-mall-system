package com.fan.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.pojo.EasyUITreeNode;
import com.fan.common.utils.E3Result;
import com.fan.e3.content.service.ContentCategoryService;

@Controller
public class ContentCatController {

	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(name = "id", defaultValue = "0") Long parentId) {
		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	/**
	 * 添加分类节点
	 * @param parentId
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result createContentCatList(Long parentId,String name) {
		//调用服务添加节点
		E3Result result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	
	/**
	 * 重命名分类节点
	 * @param id
	 * @param name
	 * @return
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateContentCatList(Long id,String name) {
		E3Result result = contentCategoryService.updateContentCategory(id, name);
		return result;
	}
	
	/**
	 * 删除分类节点
	 * @param id
	 * @return
	 */
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteContentCatList(Long id) {
		E3Result result = contentCategoryService.deleteContentCategory(id);
		return result;
	}
}
