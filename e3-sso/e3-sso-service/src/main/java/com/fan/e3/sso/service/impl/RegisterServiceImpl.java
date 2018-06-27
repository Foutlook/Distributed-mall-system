package com.fan.e3.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.fan.common.utils.E3Result;
import com.fan.e3.mapper.TbUserMapper;
import com.fan.e3.pojo.TbUser;
import com.fan.e3.pojo.TbUserExample;
import com.fan.e3.pojo.TbUserExample.Criteria;
import com.fan.e3.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;

	@Override
	public E3Result checkData(String param, int type) {
		// 1、从tb_user表中查询数据
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		// 2、查询条件根据参数动态生成。
		// 1、2、3分别代表username、phone、email
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		} else if (type == 3) {
			criteria.andEmailEqualTo(param);
		} else {
			return E3Result.build(400, "非法的参数");
		}
		// 执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		// 3、判断查询结果，如果查询到数据返回false。
		if (list == null || list.size() == 0) {
			// 4、如果没有返回true。
			return E3Result.ok(true);
		}
		// 5、使用e3Result包装，并返回。
		return E3Result.ok(false);
	}

	/**
	 * 插入数据
	 */
	@Override
	public E3Result register(TbUser user) {
		// 1、使用TbUser接收提交的请求。
		if (StringUtils.isBlank(user.getUsername())) {
			return E3Result.build(400, "用户名不能为空");
		}
		if (StringUtils.isBlank(user.getPassword())) {
			return E3Result.build(400, "密码不能为空");
		}
		if (StringUtils.isBlank(user.getPhone())) {
			return E3Result.build(400, "手机号不能为空");
		}
		// 校验数据是否可用
		E3Result result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3Result.build(400, "此用户名已经被使用");
		}
		// 校验电话是否可以
		if (StringUtils.isNotBlank(user.getPhone())) {
			result = checkData(user.getPhone(), 2);
			if (!(boolean) result.getData()) {
				return E3Result.build(400, "此手机号已经被使用");
			}
		}
		// 校验email是否可用
		if (StringUtils.isNotBlank(user.getEmail())) {
			result = checkData(user.getEmail(), 3);
			if (!(boolean) result.getData()) {
				return E3Result.build(400, "此邮件地址已经被使用");
			}
		}
		// 2、补全TbUser其他属性。
		user.setCreated(new Date());
		user.setUpdated(new Date());
		// 3、密码要进行MD5加密。
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		// 4、把用户信息插入到数据库中。
		userMapper.insert(user);
		// 5、返回e3Result。
		return E3Result.ok();
	}

}
