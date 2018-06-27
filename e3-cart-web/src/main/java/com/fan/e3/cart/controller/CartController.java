package com.fan.e3.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.utils.CookieUtils;
import com.fan.common.utils.E3Result;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.cart.service.CartService;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbUser;
import com.fan.e3.service.ItemService;

@Controller
public class CartController {

	@Value("${COOKIE_CART_NAME}")
	private String COOKIE_CART_NAME;
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;

	@Autowired
	private ItemService itemService;
	@Autowired
	private CartService cartService;

	/**
	 * 添加购物车
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCartItem(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		//判断用户是否登录
		TbUser user  = (TbUser) request.getAttribute("user");
		if(user!=null){
			//如果是登录状态，保存到redis中
			cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		// 1、从cookie中查询商品列表。
		List<TbItem> cartList = getCartList(request);
		// 2、判断商品在商品列表中是否存在。
		boolean hasItem = false;
		for (TbItem tbItem : cartList) {
			// 对象比较的是地址，应该是值的比较
			if (tbItem.getId() == itemId.longValue()) {
				// 3、如果存在，商品数量相加。
				tbItem.setNum(tbItem.getNum() + num);
				hasItem = true;
				break;
			}
		}
		if (!hasItem) {
			// 4、不存在，根据商品id查询商品信息。
			TbItem tbItem = itemService.getTbItemById(itemId);
			// 取一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNoneBlank(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
			// 设置购买商品数量
			tbItem.setNum(num);
			// 5、把商品添加到购车列表。
			cartList.add(tbItem);
		}
		// 6、把购车商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_NAME, JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE,
				true);
		return "cartSuccess";
	}

	/**
	 * 从cookie中取购物车列表
	 * 
	 * @param request
	 * @return
	 */
	private List<TbItem> getCartList(HttpServletRequest request) {
		// 取购物车列表
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_NAME, true);
		// 判断json是否为null
		if (StringUtils.isNotBlank(json)) {
			// 把json转换成商品列表返回
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}

	/**
	 * 显示购物车的请求
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request, Model model) {
		// 取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		// 传递给页面
		model.addAttribute("cartList", cartList);
		return "cart";
	}

	/**
	 * 跟新购物车中的商品数量
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3Result updateNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		// 1、接收两个参数
		// 2、从cookie中取商品列表
		List<TbItem> cartList = getCartList(request);
		// 3、遍历商品列表找到对应商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 4、更新商品数量
				tbItem.setNum(num);
			}
		}
		// 5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_NAME, JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE,
				true);
		// 6、响应e3Result。Json数据。
		return E3Result.ok();
	}

	/**
	 * 购物车删除商品
	 * @param itemId
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		// 1、从url中取商品id
		// 2、从cookie中取购物车商品列表
		List<TbItem> cartList = getCartList(request);
		// 3、遍历列表找到对应的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId() == itemId.longValue()) {
				// 4、删除商品。
				cartList.remove(tbItem);
				break;
			}
		}
		// 5、把商品列表写入cookie。
		CookieUtils.setCookie(request, response, COOKIE_CART_NAME, JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		// 6、返回逻辑视图：在逻辑视图中做redirect跳转。
		return "redirect:/cart/cart.html";
	}

}
