package com.kanq.train.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.web.entity.RequestUser;

import cn.hutool.core.lang.Caller;

/**
 * 1. 此类只负责将用户相关信息存放在一个集中的位置(<code>Request.java</code>),<br>
 * 2. 便于其他地方使用( <strong><code>Request.getUser()</code>和<code>Request.getUserIfLogin()</code></strong> ) <br>
 * 
 * @author LQ
 *
 */
public class UserInfoShiroFilter extends PathMatchingFilter {

	private static final Logger LOG = LoggerFactory.getLogger(UserInfoShiroFilter.class);

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		if (LOG.isDebugEnabled()) {
			LogUtils.logDebugInfo(LOG, this, request);
			LOG.debug("### [ {} ] execute [ fiter.onPreHandle ]", Caller.getCaller().getSimpleName());
		}

		// 我们不负责新建session,此类只负责将用户相关信息存放在一个集中的位置(UserUtil),便于其他地方使用( UserUtil.getUser() )		
		Session session = SecurityUtils.getSubject().getSession(false);

		//		final HttpSession session = ((HttpServletRequest) request)
		//				.getSession(false);
		if (null == session) {
			LOG.debug("### [Shiro] because the session is [ null ], so let it pass. ");
			return true;
		}

		final RequestUser currentUser = RequestUser.extractUserInfo(session);
		if (null == currentUser) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("### there is no userInfo in session, so let it pass. ");
			}

			return true;
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("### put the userInfo [ {} ] into session and threadContext. ", currentUser);
		}

		RequestUser.setUser(currentUser);

		return true;
	}

	@Override
	protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
		if (LOG.isDebugEnabled()) {
			LogUtils.logDebugInfo(LOG, this, request);
			LOG.debug("### [ {} ] execute [ fiter.postHandle ]", Caller.getCaller().getSimpleName());
		}

		// 清空
		RequestUser.clearAllUserInfo();

		if (LOG.isDebugEnabled()) {
			LOG.debug("### remove info from ThreadContext successfully.");
		}

		super.postHandle(request, response);
	}

}