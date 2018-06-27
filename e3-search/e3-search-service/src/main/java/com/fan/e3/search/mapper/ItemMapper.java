package com.fan.e3.search.mapper;

import java.util.List;

import com.fan.common.pojo.SearchItem;

public interface ItemMapper {
	List<SearchItem> getItemList();
	SearchItem getItemById(Long itemId);
}
