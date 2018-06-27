package com.fan.e3.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fan.common.jedis.JedisClient;
import com.fan.common.pojo.EasyUIDataGridResult;
import com.fan.common.utils.E3Result;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.content.service.ContentService;
import com.fan.e3.mapper.TbContentMapper;
import com.fan.e3.pojo.TbContent;
import com.fan.e3.pojo.TbContentExample;
import com.fan.e3.pojo.TbContentExample.Criteria;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	// 根据categoryId查询内容
	@Override
	public EasyUIDataGridResult getContent(long categoryId, int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbContentExample contentExample = new TbContentExample();
		Criteria criteria = contentExample.createCriteria();
		// 设置查询条件
		criteria.andCategoryIdEqualTo(categoryId);
		// 查询
		List<TbContent> list = contentMapper.selectByExample(contentExample);
		// 取分页信息
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		// 创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	/**
	 * 添加内容
	 */
	@Override
	public E3Result addContent(TbContent content) {
		// 补全pojo
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//缓存同步
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3Result.ok();
	}

	/**
	 * 根据内容分类id查询内容列表
	 */
	@Override
	public List<TbContent> getContentListByCId(long categoryId) {
		// 查询缓存，
		try {
			// 如果缓存中有直接响应结果
			String json = jedisClient.hget(CONTENT_LIST, categoryId+"");
			if(StringUtils.isNotBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 没有直接查询数据库

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		// 设置查询条件
		criteria.andCategoryIdEqualTo(categoryId);
		// 执行查询
		List<TbContent> list = contentMapper.selectByExample(example);

		// 把结果添加到缓存
		try {
			jedisClient.hset(CONTENT_LIST, categoryId + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
