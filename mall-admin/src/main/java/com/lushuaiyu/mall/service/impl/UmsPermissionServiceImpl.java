package com.lushuaiyu.mall.service.impl;

import com.lushuaiyu.mall.entity.UmsPermission;
import com.lushuaiyu.mall.mapper.UmsPermissionMapper;
import com.lushuaiyu.mall.service.UmsPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 服务实现类
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Service
public class UmsPermissionServiceImpl extends ServiceImpl<UmsPermissionMapper, UmsPermission> implements UmsPermissionService {

    @Override
    public List<UmsPermission> findUserPermissions(String username) {
        return null;
    }
}
