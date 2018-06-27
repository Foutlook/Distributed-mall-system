package com.fan.e3.cart.service;

import java.util.List;

import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbItem;

public interface CartService {
	E3Result addCart(long userId,long itemId,int num);
	E3Result mergeCart(long userId,List<TbItem> itemList);
}
