package com.kanq.train.shiro.realm;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kanq.train.service.LoginService;
import com.kanq.train.util.ConstantsUtil;
import com.kanq.web.entity.RequestUser;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;

/**
 * 授权和鉴权 <br>
 * 
 * 将必要的数据存入Session
 */
public class UpmsRealm extends AuthorizingRealm {

	private static Logger LOG = LoggerFactory.getLogger(UpmsRealm.class);

	@Resource(name = "loginService")
	private LoginService loginService;

	public UpmsRealm() {
		// 本UpmsRealm只负责处理这个KanqUsernamePasswordToken
		this.setAuthenticationTokenClass(KanqUsernamePasswordToken.class);
	}

	// 是否支持该token
	@Override
	public boolean supports(AuthenticationToken token) {
		return super.supports(token);
	}

	// SecurityUtils.getSubject().logout(); 时会回调本方法
	@Override
	public void onLogout(PrincipalCollection principals) {
		final RequestUser user = RequestUser.getUser();

		// 记录登出的日志
		if (LOG.isDebugEnabled()) {
			LOG.debug("记录 [ {} ] 相关信息作为登出日志到二级存储中. ", user);
		}
		try {
			this.loginService.logout(user);
		} catch (Exception e) {
			throw ExceptionUtil.wrapRuntime(e);
		}

		super.onLogout(principals);
	}

	/**
	* ------------------------------------ 认证/鉴权：登录时调用 <br>
	* 验证当前登录的Subject
	* @see 经测试:本例中该方法的调用时机为LoginController.login()方法中执行Subject.login()时
	*  
	* @param token
	* @return
	* @throws AuthenticationException
	*/
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		final String username = (String) token.getPrincipal();
		final String password = new String((char[]) token.getCredentials());

		if (LOG.isDebugEnabled()) {
			LOG.debug("对用户[ {} ]进行登录验证...验证开始", username);
		}

		RequestUser upmsUser = validateUpmsUser((KanqUsernamePasswordToken) token);
		if (null == upmsUser) {
			throw new AuthenticationException();
		}

		// ------------ 通用逻辑是:
		// 1. 使用用户名去查询出相关的记录
		// 2. 然后使用用户传递过来的密码和数据库里查出来的salt, 一起进行MD5加密; 将加密结果和数据库存储的密码进行比对
		// 3. 上面的哪一步不满足都说明不通过.

		// 查询用户信息
		// UpmsUser upmsUser =
		// upmsApiService.selectUpmsUserByUsername(username);

		// if (!upmsUser.getPassword().equals(
		// MD5Util.MD5(password + upmsUser.getSalt()))) {
		// throw new IncorrectCredentialsException();
		// }

		// 如果帐号已经被锁
		// if (upmsUser.isLocked()) {
		// throw new LockedAccountException();
		// }

		// 将必要的数据存入Session
		KanqUsernamePasswordToken tokenDirectly = (KanqUsernamePasswordToken) token;
		this.saveUserInfoToSession(upmsUser, tokenDirectly);

		// 因为我们没有salt ;;; , Md5Utils.hash(password), ByteSource.Util.bytes("")
		return new SimpleAuthenticationInfo(username,password, getName());
	}

	private RequestUser validateUpmsUser(KanqUsernamePasswordToken authenticationToken) {
		final String username = (String) authenticationToken.getPrincipal();
		final String password = new String((char[]) authenticationToken.getCredentials());
		//final String loginType = authenticationToken.getLoginType();

		if (LOG.isDebugEnabled()) {
			LOG.debug("begin validate user [ {} ] and password [ {} ] from db.", username, password);
		}

		// FIXME 如果需要进行5次密码校验的话, 则需要这个查询进行修改, 只使用用户名进行查询; 而不是一起进行查询

		Map<String, Object> loginResult = loginService.login(BeanUtil.beanToMap(authenticationToken));
		if (null == loginResult) {
			return null;
		}

		return RequestUser.of(loginResult);
	}

	/**
	 * 将一些数据放到ShiroSession中,以便于其它地方使用
	 * @see 比如Controller,使用时直接用HttpSession.getAttribute(key)就可以取到
	 */
	private void saveUserInfoToSession(RequestUser upmsUser, KanqUsernamePasswordToken authenticationToken) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null == currentUser) {
			return;
		}

		Session session = currentUser.getSession();
		if (null == session) {
			return;
		}

		LOG.debug("Session默认超时时间为[ {} ] 毫秒", session.getTimeout());

		RequestUser.insertUserInfoToSession(session, upmsUser);

		//根据用户ID查询用户拥有的角色
		List<Map<String, Object>> roleList = loginService.getRoleListByUserId(BeanUtil.beanToMap(upmsUser));
		if (roleList != null && roleList.size() > 0) {
			session.setAttribute(ConstantsUtil.CURRENT_USER_ROLE, roleList);
		}
	}

	/**
	 * ------------------------------------  授权：验证权限时调用;该方法是在鉴权前被调用的, 所以我们可以这里对请求的资源进行授权, 
	 * 	1. 因为本方法只会在AuthorizingRealm类的getAuthorizationInfo中调用
	 * 	2. 而AuthorizingRealm类的getAuthorizationInfo方法所出现的位置都是在代码里的checkXXX, hasXXX, isXXX时调用
	 * 
	 * 为当前登录的Subject授予角色和权限,
	 * @see 经测试:本例中该方法的调用时机为需授权资源被访问时 (subject.checkPermission/checkRole/hasRole时; 或者说spring-shiro-web.xml文件中的ShiroFilterFactoryBean中的filterChainDefinitions属性值中的)
	 * @see 经测试:并且每次访问需授权资源时都会执行该方法中的逻辑,这表明本例中默认并未启用AuthorizationCache
	 * @see 个人感觉若使用了Spring3.1开始提供的ConcurrentMapCache支持,则可灵活决定是否启用AuthorizationCache
	 * @see 比如说这里从数据库获取权限信息时,先去访问Spring3.1提供的缓存,而不使用Shior提供的AuthorizationCache
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		//final String username = (String) super.getAvailablePrincipal(principals);
		//		// 查出当前用户信息
		//		final UpmsUser upmsUser = upmsApiService
		//				.selectUpmsUserByUsername(username);
		//
		//		// 当前用户所有角色
		//		List<UpmsRole> upmsRoles = upmsApiService
		//				.selectUpmsRoleByUpmsUserId(upmsUser.getUserId());
		//		Set<String> roles = new HashSet<>();
		//		for (UpmsRole upmsRole : upmsRoles) {
		//			if (StringUtil.isNotBlank(upmsRole.getName())) {
		//				roles.add(upmsRole.getName());
		//			}
		//		}
		//
		//		// 当前用户所有权限
		//		List<UpmsPermission> upmsPermissions = upmsApiService
		//				.selectUpmsPermissionByUpmsUserId(upmsUser.getUserId());
		//		Set<String> permissions = new HashSet<>();
		//		for (UpmsPermission upmsPermission : upmsPermissions) {
		//			if (StringUtil.isNotBlank(upmsPermission.getPermissionValue())) {
		//				permissions.add(upmsPermission.getPermissionValue());
		//			}
		//		}

		/*
				 if(Boolean.TRUE.equals(user.getLocked())) {
		        throw new LockedAccountException(); //帐号锁定
		    }
		
		    //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		    SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
		            user.getUsername(), //用户名
		            user.getPassword(), //密码
		            ByteSource.Util.bytes(user.getCredentialsSalt()),//salt=username+salt
		            getName()  //realm name
		    );
		    return authenticationInfo;
		*/
		SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
		simpleAuthorizationInfo.setStringPermissions(null);
		simpleAuthorizationInfo.setRoles(null);
		return simpleAuthorizationInfo;
	}

	// --------------------------------------------------------------------------- Cache

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
}