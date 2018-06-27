package com.fan.e3.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.fan.common.jedis.JedisClient;
import com.fan.common.pojo.EasyUIDataGridResult;
import com.fan.common.utils.E3Result;
import com.fan.common.utils.IDUtils;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.mapper.TbItemDescMapper;
import com.fan.e3.mapper.TbItemMapper;
import com.fan.e3.mapper.TbItemParamItemMapper;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbItemDesc;
import com.fan.e3.pojo.TbItemDescExample;
import com.fan.e3.pojo.TbItemExample;
import com.fan.e3.pojo.TbItemExample.Criteria;
import com.fan.e3.pojo.TbItemParamItem;
import com.fan.e3.pojo.TbItemParamItemExample;
import com.fan.e3.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination topicDestination;
	@Autowired
	private JedisClient jedisClient;
	@Value("${ITEM_INFO_PRE}")
	private String ITEM_INFO_PRE;
	@Value("${ITEM_INFO_EXPIRE}")
	private Integer ITEM_INFO_EXPIRE;

	// 根据主键查询
	@Override
	public TbItem getTbItemById(Long itemId) {
		try {
			// 查询缓存
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":BASE");
			if (StringUtils.isNotBlank(json)) {
				// 把json转换为java对象
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 方式一: 根据主键查询
		// TbItem item = itemMapper.selectByPrimaryKey(id);
		// 方式二: 可以设置查询条件
		TbItemExample itemExample = new TbItemExample();
		Criteria criteria = itemExample.createCriteria();
		// 设置查询条件
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(itemExample);
		if (list != null && list.size() > 0) {
			TbItem item = list.get(0);
			try {
				// 把数据保存到缓存
				jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(item));
				// 设置缓存的有效期
				jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":BASE", ITEM_INFO_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return item;
		}
		return null;
	}

	// 查询商品
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		// 创建返回结果对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	// 插入商品
	@Override
	public E3Result addItem(TbItem item, String desc) {
		// 生成商品ID
		final long itemId = IDUtils.genItemId();
		// 补全item的属性
		item.setId(itemId);
		// 1-正常，2-下架，3-删除'
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 插入商品到商品表
		itemMapper.insert(item);
		// 创建商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全属性
		itemDesc.setItemId(itemId);
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 向商品描述表插入数据
		itemDescMapper.insert(itemDesc);

		// 返回结果前发出消息，通知同步索引库
		jmsTemplate.send(topicDestination, new MessageCreator() {

			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		// 返回成功
		return E3Result.ok();
	}

	// 查询商品描述
	@Override
	public TbItemDesc getItemDesc(Long itemID) {
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemID);

		return itemDesc;
	}

	// 查询商品规格
	@Override
	public List<TbItemParamItem> getItemParamItem(Long itemID) {
		// 添加查询条件
		TbItemParamItemExample example = new TbItemParamItemExample();
		com.fan.e3.pojo.TbItemParamItemExample.Criteria criteria = example.createCriteria();
		// 添加查询条件
		criteria.andItemIdEqualTo(itemID);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExample(example);
		return list;
	}

	// 更新商品
	@Override
	public E3Result updateItem(TbItem item, String desc) {
		// 1-正常，2-下架，3-删除'
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 添加更新条件
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(item.getId());
		itemMapper.updateByExample(item, example);

		// 创建商品描述表对应的pojo对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 补全属性
		itemDesc.setItemId(item.getId());
		itemDesc.setItemDesc(desc);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 添加更新条件
		TbItemDescExample descExample = new TbItemDescExample();
		com.fan.e3.pojo.TbItemDescExample.Criteria createCriteria = descExample.createCriteria();
		createCriteria.andItemIdEqualTo(item.getId());
		itemDescMapper.updateByExample(itemDesc, descExample);

		return E3Result.ok();
	}

	// 删除商品，可以一次删除多个
	@Override
	public E3Result deleteItem(String ids) {
		// 处理ids
		String[] split = ids.split(",");
		for (String id : split) {
			itemMapper.deleteByPrimaryKey(Long.parseLong(id));
		}
		return E3Result.ok();
	}

	// 下架商品
	@Override
	public E3Result instockItem(String ids) {
		/*
		 * TbItemExample example = new TbItemExample(); Criteria criteria =
		 * example.createCriteria(); System.out.println(ids);
		 */
		// 处理ids
		String[] split = ids.split(",");
		for (String id : split) {
			System.out.println(id + "----------");

			// 查出商品修改status
			TbItem tbItem = itemMapper.selectByPrimaryKey(Long.parseLong(id));
			tbItem.setStatus((byte) 2);
			// 增加条件
			// criteria.andIdEqualTo(Long.parseLong(id));
			// 执行
			// itemMapper.updateByExample(tbItem, example);
			itemMapper.updateByPrimaryKey(tbItem);
		}

		return E3Result.ok();
	}

	// 上架商品
	@Override
	public E3Result reshelfItem(String ids) {
		/*
		 * TbItemExample example = new TbItemExample(); Criteria criteria =
		 * example.createCriteria();
		 */
		// 处理ids
		String[] split = ids.split(",");
		for (String id : split) {
			// 查出商品修改status
			TbItem tbItem = itemMapper.selectByPrimaryKey(Long.parseLong(id));
			tbItem.setStatus((byte) 1);
			// 增加条件
			// criteria.andIdEqualTo(Long.parseLong(id));
			// 执行
			// itemMapper.updateByExample(tbItem, example);
			itemMapper.updateByPrimaryKey(tbItem);
		}

		return E3Result.ok();
	}

	// 取商品描述
	@Override
	public TbItemDesc geItemDescById(long itemId) {
		try {
			// 查询缓存
			String json = jedisClient.get(ITEM_INFO_PRE + ":" + itemId + ":DESC");
			if (StringUtils.isNotBlank(json)) {
				// 把json转换为java对象
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TbItemDesc tbItemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		
		try {
			// 把数据保存到缓存
			jedisClient.set(ITEM_INFO_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(tbItemDesc));
			// 设置缓存的有效期
			jedisClient.expire(ITEM_INFO_PRE + ":" + itemId + ":DESC", ITEM_INFO_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tbItemDesc;
	}

}
