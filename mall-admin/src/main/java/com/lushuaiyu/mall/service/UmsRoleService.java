package com.lushuaiyu.mall.service;

import com.lushuaiyu.mall.entity.UmsRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务类
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
public interface UmsRoleService extends IService<UmsRole> {
    /**
     * 通过用户名查找用户角色
     *
     * @param userName 用户名
     * @return 用户角色集合
     */
    List<UmsRole> findUserRole(String userName);

}
