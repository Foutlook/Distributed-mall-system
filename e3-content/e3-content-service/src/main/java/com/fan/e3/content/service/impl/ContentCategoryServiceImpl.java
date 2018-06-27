package com.fan.e3.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fan.common.pojo.EasyUITreeNode;
import com.fan.common.utils.E3Result;
import com.fan.e3.content.service.ContentCategoryService;
import com.fan.e3.mapper.TbContentCategoryMapper;
import com.fan.e3.pojo.TbContentCategory;
import com.fan.e3.pojo.TbContentCategoryExample;
import com.fan.e3.pojo.TbContentCategoryExample.Criteria;

/**
 * @TODO 内容分类管理
 * @author foutlook
 * @DATE 2018年2月21日
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
	
	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	//查询内容分类
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			//添加到list中
			nodeList.add(node);
		}
		return nodeList;
	}

	//插入商品分类
	@Override
	public E3Result addContentCategory(long parentId, String name) {
		//创建一个tb_content_category表对应的pojo对象
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo属性
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//1(正常),2(删除)'
		contentCategory.setStatus(1);
		//默认排序是1
		contentCategory.setSortOrder(1);
		//新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		//插入到数据库
		contentCategoryMapper.insert(contentCategory);
		//判断父节点的isparent属性，如果不是true改为true
		//根据parentId查询父节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if(!parent.getIsParent()){
			parent.setIsParent(true);
			//更新到数据库
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//返回结果，返回E3Result包含id
		return E3Result.ok(contentCategory);
	}

	//重命名分类节点
	@Override
	public E3Result updateContentCategory(long id, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setId(id);
		contentCategory.setName(name);
		contentCategoryMapper.updateByPrimaryKeySelective(contentCategory);
		return E3Result.ok();
	}

	//删除分类节点
	@Override
	public E3Result deleteContentCategory(long id) {
		//判断节点是否有子节点，有子节点禁止删除
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		Boolean isParent = contentCategory.getIsParent();
		if(isParent){
			E3Result e3Result = new E3Result(500, "error", null);
			return e3Result;
		}
		//如果没有则删除
		contentCategoryMapper.deleteByPrimaryKey(id);
		//判断被删除的父节点是否还有子节点
		Long parentId = contentCategory.getParentId();
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//添加查询条件
		criteria.andParentIdEqualTo(parentId);
		//计算有父节点id的个数
		int countByExample = contentCategoryMapper.countByExample(example);
		//没有子节点修改isparent属性
		if(countByExample==0){
			TbContentCategory parentContent = contentCategoryMapper.selectByPrimaryKey(parentId);
			parentContent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKeySelective(parentContent);
		}
		return E3Result.ok();
	}
	
}
