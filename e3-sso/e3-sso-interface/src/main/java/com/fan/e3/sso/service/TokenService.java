package com.fan.e3.sso.service;

import com.fan.common.utils.E3Result;

public interface TokenService {
	E3Result getUserByToken(String token);
}
