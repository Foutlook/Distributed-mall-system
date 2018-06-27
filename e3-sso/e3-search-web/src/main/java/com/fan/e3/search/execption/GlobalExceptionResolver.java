package com.fan.e3.search.execption;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 全局异常处理器
 * @TODO 
 * @author foutlook
 * @DATE 2018年2月25日
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object obj,
			Exception exception) {
		//打印控制台
		exception.printStackTrace();
		//写日志
		logger.debug("测试输出的日志...");
		logger.info("系统发生异常了...");
		logger.error("系统发生异常",exception);
		//发邮件，短信
		//使用jmail工具包。短信使用第三方的webservice
		//显示错误
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
