package com.kanq.train.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.qd.core.invoke.ServiceContext;

public final class MixRequestUserInfo implements RequestUserInfo {

	private static final Logger LOG = LoggerFactory.getLogger(MixRequestUserInfo.class);
	
	private static MixRequestUserInfo INSTANCE = new MixRequestUserInfo();

	public static final MixRequestUserInfo getInstance() {
		return INSTANCE;
	}

	public Map<String, Object> get(HttpServletRequest request) {
		return get(request, null);
	}

	@Override
	public Map<String, Object> get(HttpServletRequest request, ServiceContext sc) {
		// 先从真实场景下获取
		RequestUserInfo rui = CurrentRequestUserInfo.INSTANCE;
		Map<String, Object> currentUserInfo = rui.get(request, sc);
		if (null != currentUserInfo) {
			return currentUserInfo;
		}
		
		LOG.debug("###[UserInfo] because there is no userInfo in Session. so we will get mock userInfo.");

		// 否则获取Mock数据
		rui = MockRequestUserInfo.INSTANCE;
		currentUserInfo = rui.get(request, sc);
		return currentUserInfo;
	}

}
