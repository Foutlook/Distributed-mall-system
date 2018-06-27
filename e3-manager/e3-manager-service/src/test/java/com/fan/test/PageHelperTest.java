package com.fan.test;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.fan.e3.mapper.TbItemMapper;
import com.fan.e3.pojo.TbItem;
import com.fan.e3.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

public class PageHelperTest {
	
	@Test
	public void testPageHelper() throws Exception{
		//初始化一个spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得Mapper容器
		TbItemMapper mapper = applicationContext.getBean(TbItemMapper.class);
		//执行SQL语句之前设置分页信息，使用PageHelper的startPage方法
		PageHelper.startPage(1, 10);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = mapper.selectByExample(example);
		//取分页信息，PageInfo   1.总记录数   2.总页数，当前页码
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getSize());
		
	}
}
