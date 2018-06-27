package com.fan.e3.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fan.common.utils.FastDFSClient;
import com.fan.common.utils.JsonUtils;

@Controller
public class PictureController {
	
	//从通过springmvc加载配置文件中得到url
	@Value("${IMAGE_SERVER_URL}")
	private String IMAGE_SERVER_URL;
	
	@RequestMapping(value="/pic/upload", produces=MediaType.TEXT_PLAIN_VALUE+";charset=utf-8")
	@ResponseBody
	public String uploadFile(MultipartFile uploadFile){
		try {
			//把图片上传到图片服务器
			FastDFSClient fastDFSClient = new FastDFSClient("classpath:conf/client.conf");
			//取文件扩展名
			String filename = uploadFile.getOriginalFilename();
			String extName = filename.substring(filename.lastIndexOf(".")+1);
			//得到一个图片的地址和文件名
			String url = fastDFSClient.uploadFile(uploadFile.getBytes(), extName);
			//补充为完整的url
			url = IMAGE_SERVER_URL+url;
			//封装到map中返回
			Map result = new HashMap<>();
			result.put("error", 0);
			result.put("url", url);
			//因为兼容性问题，这里返回把对象装换成json形式的字符串 
			return JsonUtils.objectToJson(result);
		} catch (Exception e) {
			e.printStackTrace();
			Map result = new HashMap<>();
			result.put("error", 1);
			result.put("message", "图片上传失败");
			return JsonUtils.objectToJson(result);
		}
	}
}
