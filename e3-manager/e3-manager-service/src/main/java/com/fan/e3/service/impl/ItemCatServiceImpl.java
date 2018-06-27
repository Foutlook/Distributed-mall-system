package com.fan.e3.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fan.common.pojo.EasyUITreeNode;
import com.fan.e3.mapper.TbItemCatMapper;
import com.fan.e3.pojo.TbItemCat;
import com.fan.e3.pojo.TbItemCatExample;
import com.fan.e3.pojo.TbItemCatExample.Criteria;
import com.fan.e3.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Override
	public List<EasyUITreeNode> getItemCatList(long parentId) {
		//根据parentId查询子节点列表
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//创建返回结果list
		List<EasyUITreeNode> resultList = new ArrayList<>();
		//把列表转换成EasyUITreeNode列表
		for (TbItemCat tbItemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbItemCat.getId());
			node.setText(tbItemCat.getName());
			//判断是否是父节点
			node.setState(tbItemCat.getIsParent()?"closed":"open");
			//添加到结果列表
			resultList.add(node);
		}
		
		//返回结果
		return resultList;
	}

}
