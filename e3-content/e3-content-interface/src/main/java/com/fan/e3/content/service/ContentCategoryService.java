package com.fan.e3.content.service;

import java.util.List;

import com.fan.common.pojo.EasyUITreeNode;
import com.fan.common.utils.E3Result;

public interface ContentCategoryService {
	
	//查询内容分类
	List<EasyUITreeNode> getContentCatList(long parentId);
	//插入内容分类
	E3Result addContentCategory(long parentId,String name);
	//重命名分类节点
	E3Result updateContentCategory(long id,String name);
	//删除分类节点
	E3Result deleteContentCategory(long id);
}
