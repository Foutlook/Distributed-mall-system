package com.fan.e3.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.fan.common.jedis.JedisClient;
import com.fan.common.utils.E3Result;
import com.fan.common.utils.JsonUtils;
import com.fan.e3.mapper.TbUserMapper;
import com.fan.e3.pojo.TbUser;
import com.fan.e3.pojo.TbUserExample;
import com.fan.e3.pojo.TbUserExample.Criteria;
import com.fan.e3.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {

	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${USER_INFO}")
	private String USER_INFO;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	/**
	 * 登录
	 */
	@Override
	public E3Result userlogin(String username, String password) {
		// 1、判断用户名密码是否正确。
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		// 查询用户信息
		List<TbUser> list = userMapper.selectByExample(example);
		if (list == null || list.size() == 0) {
			return E3Result.build(400, "用户名或密码错误");
		}
		TbUser user = list.get(0);
		// 校验密码
		if (!user.getPassword().equals(DigestUtils.md5DigestAsHex(password.getBytes()))) {
			return E3Result.build(400, "用户名或密码错误");
		}
		// 2、登录成功后生成token。Token相当于原来的jsessionid，字符串，可以使用uuid。
		String token = UUID.randomUUID().toString();
		// 3、把用户信息保存到redis。Key就是token，value就是TbUser对象转换成json。
		// 4、使用String类型保存Session信息。可以使用“前缀:token”为key
		user.setPassword(null);
		jedisClient.set(USER_INFO + ":" + token, JsonUtils.objectToJson(user));
		// 5、设置key的过期时间。模拟Session的过期时间。一般半个小时。
		jedisClient.expire(USER_INFO + ":" + token, SESSION_EXPIRE);
		// 6、返回e3Result包装token。
		return E3Result.ok(token);

	}

}
