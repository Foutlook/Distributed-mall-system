package com.fan.e3.solrj;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrj {
	
	@Test
	public void addDocument() throws Exception{
		//创建一个SolrServer独享，创建一个连接，参数Solr服务的url
		/*SolrServer solrServer = new HttpSolrServer("http://192.168.25.131:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域，文档中必须包含一个id域，所有的域的名称必须在schema.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品");
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();*/
	}
	
	//查询
	public void queryIndex() throws Exception{
		//创建一个SolrServer
		//创建一个SolrQuery对象
		//设置查询条件
		//执行查询 QueryResponse对象
		//取文档列表，取查询总记录数
		//遍历文档列表，从域中取出内容
	}
	
}
