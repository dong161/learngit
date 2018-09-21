package com.kanq.train.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kanq.qd.core.invoke.ServiceContext;
import com.kanq.train.util.ConstantsUtil;

public interface RequestUserInfo {

	/**
	 * 执行上下文中, 用户对应的Key
	 */
	static final String EXECUTECONTEXT_KEY_USER = ConstantsUtil.EXECUTECONTEXT_KEY_USER;
	/**
	 * 执行上下文中, 用户角色对应的Key
	 */
	static final String EXECUTECONTEXT_KEY_USER_ROLE = ConstantsUtil.EXECUTECONTEXT_KEY_USER_ROLE;

	Map<String, Object> get(HttpServletRequest request, ServiceContext sc);
}
