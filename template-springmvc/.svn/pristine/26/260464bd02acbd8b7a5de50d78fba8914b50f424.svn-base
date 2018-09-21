package com.kanq.train.shiro.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.qd.use.service.IBaseService;
import com.kanq.train.shiro.util.ShiroUtils;

import cn.hutool.core.map.MapUtil;

/**
 * <p>模块授权, 该用户是否拥有读取该页面的权限
 * <p> 按照上面的需求, 所以在2018/4/10更换思路, 改为继承自AuthorizationFilter(授权), 而非之前的AuthenticationFilter;
 * <p> 所以页面什么的, 最好有良好的分包; 否则无法优化, 也会导致无谓的配置复杂度.
 * <p> AuthorizationFilter类中自带了名为unauthorizedUrl的字段. 这也正是我们需要的.
 * <p> 既然是授权, 所以本Filter<strong>必须</strong>配置在authc之<strong>后</strong>
 * <p> 之前的思路参见 {@link KanqModuleAuthenticationFilter}
 */
public class KanqModuleAuthorizationFilter extends AuthorizationFilter {

	/*-------------------------------------------
	|             C O N S T A N T S             |
	============================================*/
	private static final Logger LOG = LoggerFactory.getLogger(KanqModuleAuthorizationFilter.class);

	/*-------------------------------------------
	|    I N S T A N C E   V A R I A B L E S    |
	============================================*/
	private IBaseService baseService;

	/*-------------------------------------------
	|  A C C E S S O R S / M O D I F I E R S    |
	============================================*/
	public void setBaseService(IBaseService baseService) {
		this.baseService = baseService;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {

		if (LOG.isDebugEnabled()) {
			LogUtils.logDebugInfo(LOG, this, request);
		}

		// 登录界面
		if (isLoginRequest(request, response)) {
			LOG.debug("### login view, so let it pass.");
			return true;
		}

		// 权限（Permission）
		final boolean hasPermissionToCurrentRequest = checkPermission(request, response);
		// 是否执行onAccessDenied的抉择逻辑位于AccessControlFilter类中的onPreHandle方法
		return hasPermissionToCurrentRequest;
	}

	// 去数据库查询用户是否拥有访问该地址的权限
	private boolean checkPermission(ServletRequest request, ServletResponse response) {
		final String pathWithinApplication = WebUtils.getPathWithinApplication(WebUtils.toHttp(request));
		final Subject currentUser = getSubject(request, response);
		// principals : 身份，即主体的标识属性**，可以是任何东西，如用户名、邮箱等，唯一即可。
		// 一个主体可以有多个principals，但只有一个Primary principals，一般是用户名/密码/手机号。
		final Object principal = currentUser.getPrincipal();

		if (LOG.isDebugEnabled()) {
			LogUtils.logDebugInfo(LOG, this, request);
			LOG.debug("### begin to validate the user [ {} ] has permission to url [ {} ]", principal,
					pathWithinApplication);
		}

		// 当前用户, 请求的菜单; 去校验是否有权限
		final Map<String, Object> queryParams = MapUtil.newMapBuilder().put("principal", principal)
				.put("url", pathWithinApplication).build();
		final Integer count = baseService.<Integer>selectOneDirect("com.kanq.shell.shiro.queryUserUrlRelation",
				queryParams);

		final boolean hasPermission = count > 0;
		LOG.debug("### after validate, the user [ {} ] has permission to url [ {} ] : [ {} ]", principal,
				pathWithinApplication, hasPermission);
		return hasPermission;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		// ---- 以下直接复制自AuthorizationFilter; 完善了下Ajax跳转

		Subject subject = getSubject(request, response);
		// If the subject isn't identified, redirect to login URL
		if (subject.getPrincipal() == null) {
			saveRequestAndRedirectToLogin(request, response);
		} else {
			// If subject is known but not authorized, redirect to the unauthorized URL if there is one
			// If no unauthorized URL is specified, just return an unauthorized HTTP status code
			String unauthorizedUrl = getUnauthorizedUrl();
			//SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
			if (StringUtils.hasText(unauthorizedUrl)) {
				// 唯一修改的就是这里
				ShiroUtils.issueRedirect(request, response, unauthorizedUrl);
			} else {
				WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
		return false;
	}

	@Override
	protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
		// Shiro的跳转只适合页面请求, 对于Ajax无能为力. 所以我们需要加入这个功能
		final String loginUrl = getLoginUrl();
		ShiroUtils.issueRedirect(request, response, loginUrl);
	}

}
