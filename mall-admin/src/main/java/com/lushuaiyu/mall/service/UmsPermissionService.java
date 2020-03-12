package com.lushuaiyu.mall.service;

import com.lushuaiyu.mall.entity.UmsPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 服务类
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
public interface UmsPermissionService extends IService<UmsPermission> {

    /**
     * 查找用户权限集
     *
     * @param username 用户名
     * @return 用户权限集合
     */
    List<UmsPermission> findUserPermissions(String username);

}
