package com.fan.e3.sso.service;

import com.fan.common.utils.E3Result;
import com.fan.e3.pojo.TbUser;

public interface RegisterService {
	
	E3Result checkData(String param,int type);
	E3Result register(TbUser user);
}
