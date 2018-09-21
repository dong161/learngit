package com.kanq.train.controller;

import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanq.qd.use.entity.FriendlyException;
import com.kanq.qd.use.entity.ResponseBean;
import com.kanq.support.util.HtmlUtils;
import com.kanq.train.service.LoginService;
import com.kanq.train.shiro.realm.KanqUsernamePasswordToken;
import com.kanq.train.util.ConstantsUtil;
import com.kanq.web.entity.RequestUser;
import com.kanq.web.utils.CookieUtil;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.map.MapUtil;

/**
 * From : <br>
 * http://jadyer.cn/2013/09/30/springmvc-shiro/
 * 
 * <br>
 * <br>
 * 
 * 本类只负责登录和登出.
 */
@RestController
@RequestMapping("/login")
public class LoginShiroController {

	private static final Logger LOG = LoggerFactory.getLogger(LoginShiroController.class);

	@Resource(name = "loginService")
	private LoginService loginService;

	// -------------------------------------------------------------- Login

	@PostMapping(value = "/login")
	public ResponseBean<Map<String, Object>> login(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		//用户名
		final String username = request.getParameter("username");
		// ---------------------------- 开始验证用户名和密码
		final KanqUsernamePasswordToken token = constructShiroToken(request);
		if (LOG.isDebugEnabled()) {
			LOG.debug("为验证登录用户而封装的Token：[ {} ]", token);
		}

		final String loginAuthenticateResult = shiroLoginAuthenticate(token);
		// ---------------------------- 验证是否登录成功
		// 获取当前的Subject
		final Subject currentUser = SecurityUtils.getSubject();
		if (!currentUser.isAuthenticated()) {
			final String loginFailReason = loginAuthenticateResult;

			if (LOG.isDebugEnabled()) {
				LOG.debug("用户[ {} ]登录失败, 原因如下: [ {} ]", username, loginFailReason);
			}

			token.clear();
			return ResponseBean.of(FriendlyException.of(loginFailReason));
		}

		// -- 登录验证成功之后
		// 处理rememberMe
		this.dealRememberMe(token, request, response);

		if (LOG.isDebugEnabled()) {
			LOG.debug("### 用户[ {} ]登录认证通过, 准备跳转到[ {} ]页面.现在开始进行认证通过后的系统参数初始化操作...", username, "主");
		}

		Map<String, Object> resultMap = MapUtil.<String, Object>of("result", ConstantsUtil.LOGIN_SUCCESS);
		return ResponseBean.of(resultMap);
	}

	private KanqUsernamePasswordToken constructShiroToken(HttpServletRequest request) {
		//用户名
		final String username = request.getParameter("username");
		//密码
		final String password = request.getParameter("password");
		//是否记住密码
		final String isRemember = request.getParameter("isremember");

		KanqUsernamePasswordToken token = new KanqUsernamePasswordToken(username, password, "");
		token.setRememberMe(("true".equalsIgnoreCase(isRemember)));
		token.setHost(HtmlUtils.getClientIpAddress(request));

		return token;
	}

	private String shiroLoginAuthenticate(KanqUsernamePasswordToken token) {
		final String username = token.getUsername();
		// 获取当前的Subject
		final Subject currentUser = SecurityUtils.getSubject();
		final StringBuilder sb = new StringBuilder();
		try {

			LOG.debug("### 对用户[ {} ]进行登录验证...验证开始", username);

			// 在调用了login方法后，SecurityManager会收到AuthenticationToken，并将其发送给已配置的Realm执行必须的认证检查
			// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
			// 所以这一步在调用login(token)方法时，它会走到MyRealm.doGetAuthenticationInfo()方法中，具体验证方式详见此方法

			// ----- 这里将要抛出的异常都是我们在自定义Realm中的doGetAuthenticationInfo()方法中抛出的
			currentUser.login(token);

			LOG.debug("### 对用户[ {} ]进行登录验证...验证通过", username);

		} catch (UnknownAccountException uae) {
			LOG.info("### 对用户[ {} ]进行登录验证...验证未通过，未知账户.", username);
			sb.append("未知账户");
		} catch (IncorrectCredentialsException ice) {
			LOG.info("### 对用户[ {} ]进行登录验证...验证未通过，错误的凭证", username);
			sb.append("密码不正确");
		} catch (LockedAccountException lae) {
			LOG.info("### 对用户[ {} ]进行登录验证...验证未通过，账户已锁定", username);
			sb.append("账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			LOG.info("### 对用户[ {} ]进行登录验证...验证未通过，错误次数过多", username);
			sb.append("用户名或密码错误次数过多");
		} catch (AuthenticationException ae) {
			// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
			LOG.info("### 对用户[ {} ]进行登录验证...验证未通过，堆栈轨迹如下: {} {}", username, System.lineSeparator(),
					ExceptionUtil.getMessage(ae));

			sb.append("用户名或密码不正确");
		}

		return sb.toString();
	}

	private void dealRememberMe(KanqUsernamePasswordToken token, HttpServletRequest request,
			HttpServletResponse response) {
		if (token.isRememberMe()) {
			//登录成功后，保存用户名到cookie
			saveUserNameCookie(response, token);
		} else {
			//清除密码
			clearUserNameCookie(request, response);
		}
	}

	private void saveUserNameCookie(HttpServletResponse response, KanqUsernamePasswordToken token) {
		String username = token.getUsername();
		CookieUtil.setCookie(response, ConstantsUtil.COOKIE_DOMAIN_NAME, username);
	}

	/**
	 * 销毁cookie
	 * @param request
	 * @param response
	 */
	private void clearUserNameCookie(HttpServletRequest request, HttpServletResponse response) {
		CookieUtil.removeCookie(response, ConstantsUtil.COOKIE_DOMAIN_NAME);
	}

	// -------------------------------------------------------------- Logout

	@PostMapping(value = "/logout")
	public ResponseBean<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final RequestUser currentUser = RequestUser.getUser();
		if (LOG.isDebugEnabled()) {
			LOG.debug("### 用户[ {} ]准备登出...", currentUser);
		}

		// 记录登出日志的操作被放到了自定义的Realm中.
		// 这里会将session失效
		SecurityUtils.getSubject().logout();

		if (LOG.isDebugEnabled()) {
			LOG.debug("### 用户[ {} ]已登出...", currentUser);
		}

		Map<String, Object> resultMap = Collections.<String, Object>singletonMap("result", "SUCCESS");
		return ResponseBean.of(resultMap);
	}
}
