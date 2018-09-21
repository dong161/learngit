package com.kanq.train.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kanq.qd.core.invoke.ServiceContext;

/**
 * Mock场景下的用户
 * @author LQ
 *
 */
class MockRequestUserInfo implements RequestUserInfo {

	static RequestUserInfo INSTANCE = new MockRequestUserInfo();

	@Override
	public Map<String, Object> get(HttpServletRequest request, ServiceContext sc) {
		final Map<String, Object> user = new HashMap<>();
		user.put(RequestUserInfo.EXECUTECONTEXT_KEY_USER, getUserInfo());
		user.put(RequestUserInfo.EXECUTECONTEXT_KEY_USER_ROLE, getRoleInfo());

		return user;
	}

	private Map<String, Object> getUserInfo() {
		final Map<String, Object> userInfo = new HashMap<>();
		userInfo.put("US_IDENT", "1");
		userInfo.put("JG_MC", "国土局");
		userInfo.put("US_CODE", "Admin");
		userInfo.put("US_NAME", "超级管理员");
		// 指示该用户是模拟数据
		userInfo.put("IS_MOCK", true);
		return userInfo;
	}

	private List<Map<String, Object>> getRoleInfo() {
		final List<Map<String, Object>> list = new ArrayList<>();

		Map<String, Object> role = new HashMap<>();
		role.put("AREA_NAME", "乔口区");
		role.put("AREA_CODE", "420104");
		role.put("AREA_ID", "420104");
		role.put("ROLE_ID", "123123123");
		list.add(role);

		role = new HashMap<>();
		role.put("AREA_NAME", "汉阳区");
		role.put("AREA_CODE", "420105");
		role.put("AREA_ID", "420105");
		role.put("ROLE_ID", "123123123");
		list.add(role);

		return list;
	}

}
