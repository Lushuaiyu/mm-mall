package com.lushuaiyu.mall.config;

import com.lushuaiyu.mall.base.Constant;
import com.lushuaiyu.mall.entity.UmsAdmin;
import com.lushuaiyu.mall.entity.UmsPermission;
import com.lushuaiyu.mall.entity.UmsRole;
import com.lushuaiyu.mall.service.UmsAdminRoleRelationService;
import com.lushuaiyu.mall.service.UmsAdminService;
import com.lushuaiyu.mall.service.UmsPermissionService;
import com.lushuaiyu.mall.service.UmsRoleService;
import com.lushuaiyu.mall.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lsy
 * @since 2018-05-03
 */
public class MyRealm extends AuthorizingRealm {
    private UmsAdminService userService;
    private UmsAdminRoleRelationService userToRoleService;
    private UmsPermissionService menuService;
    private UmsRoleService roleService;
    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (userToRoleService == null) {
            this.userToRoleService = SpringContextBean.getBean(UmsAdminRoleRelationService.class);
        }
        if (menuService == null) {
            this.menuService = SpringContextBean.getBean(UmsPermissionService.class);
        }
        if (roleService == null) {
            this.roleService = SpringContextBean.getBean(UmsRoleService.class);
        }

        String username1 = JWTUtil.getUsername(principals.toString());
        UmsAdmin user = userService.getById(username1);

        String username = user.getUsername();

        //UserToRole userToRole = userToRoleService.selectByUsername(user.getUsername());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        /*
        Role role = roleService.selectOne(new EntityWrapper<Role>().eq("role_code", userToRole.getRoleCode()));
        //添加控制角色级别的权限
        Set<String> roleNameSet = new HashSet<>();
        roleNameSet.add(role.getRoleName());
        simpleAuthorizationInfo.addRoles(roleNameSet);
        */
        //控制菜单级别按钮  类中用@RequiresPermissions("user:list") 对应数据库中code字段来控制controller
        // 获取用户角色集
        List<UmsRole> roleList = roleService.findUserRole(username);
        Set<String> roleSet = roleList.stream().map(UmsRole::getName).collect(Collectors.toSet());
        simpleAuthorizationInfo.setRoles(roleSet);

        // 获取用户权限集
        List<UmsPermission> permissionList = this.menuService.findUserPermissions(username);
        Set<String> permissionSet = permissionList.stream().map(UmsPermission::getValue).collect(Collectors.toSet());
        simpleAuthorizationInfo.setStringPermissions(permissionSet);
        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可.
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UnauthorizedException {
        if (userService == null) {
            this.userService = SpringContextBean.getBean(UmsAdminService.class);
        }
        String token = (String) auth.getCredentials();
        if(Constant.isPass){
            return new SimpleAuthenticationInfo(token, token, this.getName());
        }
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new UnauthorizedException("token invalid");
        }
        UmsAdmin userBean = userService.getById(username);
        if (userBean == null) {
            throw new UnauthorizedException("User didn't existed!");
        }
        if (! JWTUtil.verify(token, username, userBean.getPassword())) {
            throw new UnauthorizedException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
