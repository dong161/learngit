package com.kanq.train.shiro.realm;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * <p> 我们的JDK是1.7版本的.所以不能选择官方推荐的 buji-pac4j.
 * <p> 另外依据CAS-Server对Java版本的需要, 我们选择的是4.2.7 版本
 * <p> CasRealm根据CAS服务器端返回的用户身份获取相应的角色/权限信息。
 */
@SuppressWarnings("deprecation")
public class MyCasRealm extends CasRealm {

//    private UserService userService;
//
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//        authorizationInfo.setRoles(userService.findRoles(username));
//        authorizationInfo.setStringPermissions(userService.findPermissions(username));

        return authorizationInfo;
    }
}
