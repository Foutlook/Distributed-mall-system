package com.fan.e3.cart.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fan.common.jedis.JedisClient;
import com.fan.common.utils.E3Result;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.cart.service.CartService;
import com.fan.e3.mapper.TbItemMapper;
import com.fan.e3.pojo.TbItem;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public E3Result addCart(long userId, long itemId,int num) {
		//向redis中添加购物车
		//数据类型是hash key：用户id   field：商品id   value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE+":"+userId, itemId+"");
		//如果商品存在数量相加
		if(hexists){
			String json = jedisClient.hget(REDIS_CART_PRE+":"+userId, itemId+"");
			//把json转换成TBItem
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			item.setNum(item.getNum()+num);
			//写回redis
			jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
			return E3Result.ok();
		}
		//不存在，更具商品id去商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		item.setNum(num);
		String image = item.getImage();
		if(StringUtils.isNotBlank(image)){
			item.setImage(image.split(",")[0]);
		}
		//添加到购物车列表
		jedisClient.hset(REDIS_CART_PRE+":"+userId, itemId+"", JsonUtils.objectToJson(item));
		//返回成功
		return E3Result.ok();
	}

	/**
	 * 合并购物车   
	 */
	@Override
	public E3Result mergeCart(long userId, List<TbItem> itemList) {
		// TODO Auto-generated method stub
		return null;
	}

}
