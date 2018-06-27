package com.fan.e3.sso.service;

import com.fan.common.utils.E3Result;

public interface LoginService {
	E3Result userlogin(String username,String password);
}
