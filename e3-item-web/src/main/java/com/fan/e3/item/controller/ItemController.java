package com.fan.e3.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fan.e3.item.pojo.Item;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbItemDesc;
import com.fan.e3.service.ItemService;

/**
 * 商品详情页展示Controller
 * 
 * @TODO
 * @author foutlook
 * @DATE 2018年2月26日
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	public String showItemInfo(@PathVariable Long itemId, Model model) {
		// 跟据商品id查询商品信息
		TbItem tbItem = itemService.getTbItemById(itemId);
		// 把TbItem转换成Item对象
		Item item = new Item(tbItem);
		// 根据商品id查询商品描述
		TbItemDesc tbItemDesc = itemService.getItemDesc(itemId);
		// 把数据传递给页面
		model.addAttribute("item", item);
		model.addAttribute("itemDesc", tbItemDesc);
		return "item";

	}

}
