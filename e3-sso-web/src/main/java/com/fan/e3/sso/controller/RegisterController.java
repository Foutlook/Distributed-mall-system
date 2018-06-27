package com.fan.e3.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbUser;
import com.fan.e3.sso.service.RegisterService;

@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3Result checkData(@PathVariable String param, @PathVariable Integer type){
		E3Result e3Result = registerService.checkData(param, type);
		return e3Result;
	}
	
	@RequestMapping(value="/user/register", method=RequestMethod.POST)
	@ResponseBody
	public E3Result register(TbUser user) {
		E3Result result = registerService.register(user);
		return result;
	}


}
