package com.fan.e3.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.utils.E3Result;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.sso.service.TokenService;

@Controller
public class TokenController {
	
	@Autowired
	private TokenService tokenService;
	
	/*@RequestMapping(value="/user/token/{token}",
			produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback) {
		E3Result result = tokenService.getUserByToken(token);
		//响应结果之前，判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	/**
	 * 第二种方式  spring 4.1以上可以使用
	 */
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback) {
		E3Result result = tokenService.getUserByToken(token);
		//响应结果之前，判断是否为jsonp请求
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		return result;
	}
}
