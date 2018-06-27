package com.fan.e3.service;

import java.util.List;

import com.fan.common.pojo.EasyUIDataGridResult;
import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbItemDesc;
import com.fan.e3.pojo.TbItemParamItem;

public interface ItemService {
	TbItem getTbItemById(Long id);

	EasyUIDataGridResult getItemList(int page, int rows);

	// 插入商品
	E3Result addItem(TbItem item, String desc);

	// 查询商品描述
	TbItemDesc getItemDesc(Long itemID);

	// 查询商品规格
	List<TbItemParamItem> getItemParamItem(Long itemID);

	// 更新商品
	E3Result updateItem(TbItem item, String desc);

	// 删除商品
	E3Result deleteItem(String ids);

	// 下架商品
	E3Result instockItem(String ids);
	
	//上架商品
	E3Result reshelfItem(String ids);
	
	//取商品描述
	TbItemDesc geItemDescById(long itemId);
}
