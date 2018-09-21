package com.kanq.train.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.kanq.qd.use.dao.ICoreDao;
import com.kanq.train.service.LoginService;
import com.kanq.train.shiro.realm.UpmsRealm;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

	@Autowired
	@Qualifier("coreDao")
	private ICoreDao coreDao;

	@Override
	public Map<String, Object> login(Map<String, Object> params) {
		Object object = params.get("password");
		if (object instanceof char[]) { // shiro的password存储形式为char[]
			params.put("password", new String(Convert.convert(char[].class, object)));
		}
		
		Map<String,Object> of = MapUtil.<String,Object>of("USER_ID",(Object)"Admin");
		of.put("USER_NAME", "Admin");
		return of;
		//return coreDao.<Map<String, Object>>selectOneDirect("loginMapper.userLogin", params);
		
	}

	@Override
	public List<Map<String, Object>> getRoleListByUserId(Map<String, Object> params) {
		return Collections.emptyList();
	}

	@Override
	public void saveLog(Map<String, String> params) {
		coreDao.insert("com.kq.common.saveCZRZ", params);
	}

	@Override
	public void logout(Map<String, Object> params) {
		// FIXME 退出

	}

	@Override
	public void resetPassword(Map<String, Object> params) {
		// FIXME 修改密码

		// --- 清空Shiro相关缓存
		RealmSecurityManager securityManager = (RealmSecurityManager) SecurityUtils.getSecurityManager();
		UpmsRealm userRealm = (UpmsRealm) securityManager.getRealms().iterator().next();
		userRealm.clearCachedAuthenticationInfo(SecurityUtils.getSubject().getPrincipals());
	}

}
