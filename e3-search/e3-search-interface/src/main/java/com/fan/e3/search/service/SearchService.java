package com.fan.e3.search.service;

import com.fan.common.pojo.SearchResult;

public interface SearchService {
	
	SearchResult search(String keyword,int page,int rows) throws Exception;
}
