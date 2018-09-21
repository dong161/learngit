package com.kanq.train.shiro.filter;

import javax.servlet.ServletRequest;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;

import cn.hutool.core.util.StringUtil;

/*
 *
 * 鉴权 / 授权
 *
 */

class LogUtils {
	public static void logDebugInfo(Logger log, PathMatchingFilter filter, ServletRequest request) {
		logDebugInfo(log, filter, request, "", new Object[0]);
	}

	/** 
	 * 统一化日志的前缀输出信息 <br>
	 * 	### Shiro Filter [ filter ] begin filter the url [ xx/zz/yy ] ;
	 * 
	 * 
	 * @param log
	 * @param filter
	 * @param request
	 * @param format
	 * @param arg1
	 */
	public static void logDebugInfo(Logger log, PathMatchingFilter filter, ServletRequest request, String format,
			Object... arg1) {
		if (log.isDebugEnabled()) {
			log.debug("### Shiro Filter [ {} ] begin filter the url [ {} ] ;", describeFilter(filter),
					WebUtils.getPathWithinApplication(WebUtils.toHttp(request)));

			if (StringUtil.isEmpty(format)) {
				return;
			}

			logDebugInfo(log, format, arg1);
		}
	}

	private static void logDebugInfo(org.slf4j.Logger log, String format, Object... arg1) {
		log.debug(format, arg1);
	}

	private static String describeFilter(PathMatchingFilter filter){
		return filter.getClass().getSimpleName();
	}
}