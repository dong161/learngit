package com.kanq.train.support;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.qd.core.invoke.ServiceContext;
import com.kanq.qd.use.support.ExecuteContextModifier;

/**
 * 将Session信息 插入到 Service 执行上下文中
 * @author LQ
 *
 */
public class ServiceExecuteContextModifier implements ExecuteContextModifier {

	private static final Logger LOG = LoggerFactory.getLogger(ServiceExecuteContextModifier.class);

	@Override
	public Map<String, Object> servicePrepare(HttpServletRequest request, ServiceContext sc) {
		LOG.debug("### begin to execute [ ExecuteContextModifier.servicePrepare ] method");

		final Map<String, Object> map = MixRequestUserInfo.getInstance().get(request);
		sc.putExecuteContext(map);

		return null;
	}

}
