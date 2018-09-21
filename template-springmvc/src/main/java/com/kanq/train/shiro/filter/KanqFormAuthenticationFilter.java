package com.kanq.train.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.kanq.train.shiro.util.ShiroUtils;

/**
 * Shiro的跳转只适合页面请求, 对于Ajax无能为力. 所以我们需要加入这个功能
 * @author LQ
 *
 */
public class KanqFormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {
	
	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        // Shiro的跳转只适合页面请求, 对于Ajax无能为力. 所以我们需要加入这个功能
		final String loginUrl = getLoginUrl();		
        ShiroUtils.issueRedirect(request, response, loginUrl);
    }
}

