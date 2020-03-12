package com.lushuaiyu.mall.mapper;

import com.lushuaiyu.mall.entity.UmsPermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户权限表 Mapper 接口
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
public interface UmsPermissionMapper extends BaseMapper<UmsPermission> {

    /**
     * 查找用户权限集
     *
     * @param userName userName
     * @return 用户权限集合
     */
    List<UmsPermission> findUserPermissions(@Param("userName") String userName);

}
