package com.fan.e3.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.pojo.EasyUIDataGridResult;
import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbItemDesc;
import com.fan.e3.pojo.TbItemParamItem;
import com.fan.e3.service.ItemService;

@Controller
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/item/{itemId}")
	@ResponseBody   //加了这个不走视图解析器，
	public TbItem getItemById(@PathVariable Long itemId){
		TbItem tbItem = itemService.getTbItemById(itemId);
		
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows){
		//调用服务查询商品列表,
		//自动转换成json数据
		EasyUIDataGridResult itemList = itemService.getItemList(page, rows);
		return itemList;
	}
	
	/**
	 * 添加商品列表
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3Result addItem(TbItem item,String desc){
		E3Result result = itemService.addItem(item, desc);
		return result;
	}
	
	/**
	 * 查询商品描述
	 */
	@RequestMapping(value="/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result getItemDesc(@PathVariable Long itemId){
		TbItemDesc itemDesc = itemService.getItemDesc(itemId);
		E3Result result = E3Result.ok(itemDesc);
		return result;
	}
	
	/**
	 * 查询商品规格
	 */
	@RequestMapping(value="/rest/item/param/item/query/{itemId}")
	@ResponseBody
	public E3Result getItemParamItem(@PathVariable Long itemId){
		List<TbItemParamItem> list = itemService.getItemParamItem(itemId);
		E3Result result = E3Result.ok(list);
		return result;
	}
	
	/**
	 * 更新商品
	 */
	@RequestMapping(value="/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item,String desc){
		//System.out.println(item.getId()+"===========");
		E3Result e3Result = itemService.updateItem(item, desc);
		return e3Result;
	}
	
	/**
	 * 删除商品
	 */
	@RequestMapping(value="/rest/item/delete")
	@ResponseBody
	public E3Result deleteItem(String ids){
		E3Result e3Result = itemService.deleteItem(ids);
		return e3Result;
	}
	
	/**
	 * 下架商品
	 */
	@RequestMapping(value="/rest/item/instock")
	@ResponseBody
	public E3Result instockItem(String ids){
		E3Result e3Result = itemService.instockItem(ids);
		return e3Result;
	}
	
	/**
	 * status上架商品
	 */
	@RequestMapping(value="/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelfItem(String ids){
		E3Result e3Result = itemService.reshelfItem(ids);
		return e3Result;
	}
}
