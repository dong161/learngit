package com.kanq.train.shiro.realm;

import lombok.EqualsAndHashCode;

import org.apache.shiro.authc.UsernamePasswordToken;

import cn.hutool.core.util.StringUtil;

/**
 * KANQ Token
 * 
 * @author LQ
 *
 */
@EqualsAndHashCode(callSuper = false)
public final class KanqUsernamePasswordToken extends UsernamePasswordToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*--------------------------------------------
	|    I N S T A N C E   V A R I A B L E S    |
	============================================*/

	/**
	 * 登录类型
	 */
	private String loginType;

	public KanqUsernamePasswordToken(final String username, final String password, final String loginType) {
		super(username, password);
		this.loginType = loginType;
	}

	@Override
	public String toString() {
		return super.toString() + StringUtil.format("{} ; loginType = [ {} ]", System.lineSeparator(), loginType);
	}
}
