package com.fan.e3.item.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.fan.e3.item.pojo.Item;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbItemDesc;
import com.fan.e3.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class HtmlGenListener implements MessageListener {

	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@SuppressWarnings("resource")
	@Override
	public void onMessage(Message message) {
		try {
			// 创建一个模版
			// 从消息中取商品id
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long iteId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			// 根据商品id查询商品信息，商品基本信息和商品描述
			TbItem tbItem = itemService.getTbItemById(iteId);
			Item item = new Item(tbItem);
			//取商品描述
			TbItemDesc itemDesc = itemService.getItemDesc(iteId);
			// 创建一个数据集，吧商品数据分装
			Map data = new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			// 加载模版对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			// 创建一个一个输出流，指定输出的目录及文件名
			Writer writer = new FileWriter(HTML_GEN_PATH+iteId+".html");
			// 生成静态页面 
			template.process(data, writer);
			// 关闭流
			writer.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
