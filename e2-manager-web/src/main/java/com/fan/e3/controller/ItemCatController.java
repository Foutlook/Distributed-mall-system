package com.fan.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.pojo.EasyUITreeNode;
import com.fan.e3.service.ItemCatService;

/**
 * @TODO 商品分类管理Controller
 * @author foutlook
 * @DATE 2018年2月18日
 */
@Controller
public class ItemCatController {

	@Autowired
	public ItemCatService itemCatService;

	@RequestMapping("/item/cat/list")
	@ResponseBody
	public List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id", defaultValue = "0")Long parentId) {
		//System.out.println(parentId+".....................");
		//调用服务查询节点列表
		List<EasyUITreeNode> itemCatList = itemCatService.getItemCatList(parentId);
		
		return itemCatList;
	}
}
