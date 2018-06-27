package com.fan.e3.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fan.common.jedis.JedisClient;
import com.fan.common.utils.E3Result;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.pojo.TbUser;
import com.fan.e3.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public E3Result getUserByToken(String token) {
		// 2、根据token查询redis。
		String json = jedisClient.get(USER_INFO + ":" + token);
		if (StringUtils.isBlank(json)) {
			// 3、如果查询不到数据。返回用户已经过期。
			return E3Result.build(400, "用户登录已经过期，请重新登录。");
		}
		// 4、如果查询到数据，说明用户已经登录。
		// 5、需要重置key的过期时间。
		jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
		// 6、把json数据转换成TbUser对象，然后使用e3Result包装并返回。
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3Result.ok(user);
	}
}
