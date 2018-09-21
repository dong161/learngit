package com.kanq.train.shiro.util;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.config.Ini;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.qd.use.entity.ResponseBean;
import com.kanq.web.entity.RequestUser;
import com.kanq.support.util.HtmlUtils;

import cn.hutool.core.lang.Caller;

public class ShiroUtils {

	private static final Logger LOG = LoggerFactory.getLogger(ShiroUtils.class);

	/**
	 * 我们可能需要对Ajax请求进行跳转, 而Shiro提供的跳转只有针对页面的.<br>
	 * 于是有了这道封装.
	 * 
	 * @param request
	 * @param response
	 * @param redirectUrl
	 */
	public static final void issueRedirect(ServletRequest request, ServletResponse response, String redirectUrl) {
		logRedirectInfo(request, response, redirectUrl);

		// Ajax 时
		if (ShiroUtils.isAjaxRequest(WebUtils.toHttp(request))) {
			final HttpServletResponse httpResponse = WebUtils.toHttp(response);
			httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			final ResponseBean<String> rb = ResponseBean.<String>of(redirectUrl);
			rb.setCode(ResponseBean.NO_LOGIN);
			rb.setMsg("没有权限");

			HtmlUtils.writerJson(httpResponse, rb);

			return;
		}

		// 普通的, 直接使用Shiro
		try {
			WebUtils.issueRedirect(request, response, redirectUrl);
		} catch (IOException e) { // ignore
		}
	}

	// 记录跳转信息
	private static void logRedirectInfo(ServletRequest request, ServletResponse response, String redirectUrl) {
		final String currentRequestUrl = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
		final String currentShiroFilter = Caller.getCaller().getSimpleName();
		final RequestUser currentUser = RequestUser.getUserIfLogin();
		LOG.info(
				"### the request [ {} ] is not authorized. so we now redirect to [ {} ]; the filter which deal the request is [ {} ], current user is [ {} ]",
				currentRequestUrl, redirectUrl, currentShiroFilter,currentUser);
	}

	/**
	 * 是否为Ajax请求
	 * @return
	 */
	public static boolean isAjaxRequest(final HttpServletRequest request) {
		return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
	}

	/**
	   * 加载过滤配置信息
	   * @return
	   * 
	   * @deprecated 未完成！
	   */
	public static String loadFilterChainDefinitions(final String initConfigFilePath) {
		final Ini ini = Ini.fromResourcePath("classpath:" + initConfigFilePath);
		ini.getSection("base_auth");
		throw new RuntimeException("未完成！");
	}
}