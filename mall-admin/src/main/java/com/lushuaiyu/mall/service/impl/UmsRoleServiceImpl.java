package com.lushuaiyu.mall.service.impl;

import com.lushuaiyu.mall.entity.UmsRole;
import com.lushuaiyu.mall.mapper.UmsRoleMapper;
import com.lushuaiyu.mall.service.UmsRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 后台用户角色表 服务实现类
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Service
public class UmsRoleServiceImpl extends ServiceImpl<UmsRoleMapper, UmsRole> implements UmsRoleService {

    @Resource
    private UmsRoleMapper umsRoleMapper;
    @Override
    public List<UmsRole> findUserRole(String userName) {
        return umsRoleMapper.findUserRole(userName);
    }
}
