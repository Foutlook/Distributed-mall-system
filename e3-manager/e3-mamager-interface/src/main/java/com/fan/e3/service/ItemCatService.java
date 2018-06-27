package com.fan.e3.service;

import java.util.List;

import com.fan.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	List<EasyUITreeNode> getItemCatList(long parentId);
}
