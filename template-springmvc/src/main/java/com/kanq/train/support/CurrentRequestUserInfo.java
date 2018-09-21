package com.kanq.train.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.qd.core.invoke.ServiceContext;
import com.kanq.support.util.HtmlUtils;
import com.kanq.train.util.ConstantsUtil;
import com.kanq.web.utils.SessionUtil;

/**
 * 真实场景下的用户
 * @author LQ
 *
 */
class CurrentRequestUserInfo implements RequestUserInfo {

	static RequestUserInfo INSTANCE = new CurrentRequestUserInfo();

	private static final Logger LOG = LoggerFactory.getLogger(CurrentRequestUserInfo.class);

	@Override
	public Map<String, Object> get(HttpServletRequest request, ServiceContext sc) {

		Session session = getShiroSession(request);
		if (null == session) {
			return null;
		}
		@SuppressWarnings("unchecked")
		final Map<String, Object> currentUserMap = (Map<String, Object>) session.getAttribute(EXECUTECONTEXT_KEY_USER);
		if (null == currentUserMap) {
			noUserInfo(sc);
			return null;
		}

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> currentUserRole = (List<Map<String, Object>>) session
				.getAttribute(ConstantsUtil.SESSION_KEY_CURRENT_USER_ROLE);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put(EXECUTECONTEXT_KEY_USER, currentUserMap);
		paramMap.put(EXECUTECONTEXT_KEY_USER_ROLE, currentUserRole);
		return paramMap;
	}

	private void noUserInfo(ServiceContext sc) {
		if (null == sc) {
			return;
		}

		final String serviceId = sc.<String>getDirect("serviceId");
		LOG.warn("### there is no real user info when execute service [ {} ].", sc.toMap(), serviceId);
	}

	private static Session getShiroSession(HttpServletRequest request) {
		final Session session = SessionUtil.getShiroSession();

		if (null == session) {
			final Map<String, String> clientParams = HtmlUtils.getParameterMap(request);
			final String errorInfo = String.format("######Session Get Fail ; the client data is [ %s ]", clientParams);
			LOG.warn(errorInfo);
			//throw new IllegalArgumentException(errorInfo);
		}

		return session;
	}

	@SuppressWarnings("unused")
	private static HttpSession getSession(HttpServletRequest request) {
		final HttpSession session = request.getSession();
		if (null == session) {
			final Map<String, String> clientParams = HtmlUtils.getParameterMap(request);
			final String errorInfo = String.format("######Session Get Fail ; the client data is [ %s ]", clientParams);
			throw new IllegalArgumentException(errorInfo);
		}

		return session;
	}

}
