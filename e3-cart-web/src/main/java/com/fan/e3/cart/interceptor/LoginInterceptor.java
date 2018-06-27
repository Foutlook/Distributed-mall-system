package com.fan.e3.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fan.common.utils.CookieUtils;
import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbUser;
import com.fan.e3.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
		//前处理，执行handler之前执行此方法
		//返回true,放行，false ：拦截
		//1.从cookie找那个取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2.如果没有token，未登陆状态，直接放行
		if(StringUtils.isBlank(token)){
			return true;
		}
		//3.取到token，需要调用sso系统的服务，根据token取用户信息
		E3Result e3Result = tokenService.getUserByToken(token);
		//4没有取到用户信息，登陆过期，直接放行
		if(e3Result.getStatus()!=200){
			return true;
		}
		//5.取到用户信息，登陆状态
		TbUser user = (TbUser) e3Result.getData();
		//6.把用户放到信息放到request中，只需要在Controller中判断request中是否包好user信息，有放行
		request.setAttribute("user", user);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

}
