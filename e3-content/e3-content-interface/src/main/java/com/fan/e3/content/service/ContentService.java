package com.fan.e3.content.service;

import java.util.List;

import com.fan.common.pojo.EasyUIDataGridResult;
import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbContent;

public interface ContentService {
	//查询内容
	EasyUIDataGridResult getContent(long categoryId,int page, int rows);
	//添加内容
	E3Result addContent(TbContent content);
	//根据categoryId查询显示到主页
	List<TbContent> getContentListByCId(long categoryId);
}
