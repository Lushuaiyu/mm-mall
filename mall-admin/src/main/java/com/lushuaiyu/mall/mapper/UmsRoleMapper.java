package com.lushuaiyu.mall.mapper;

import com.lushuaiyu.mall.entity.UmsRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 后台用户角色表 Mapper 接口
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
public interface UmsRoleMapper extends BaseMapper<UmsRole> {

    /**
     * 通过 userName 查找用户角色
     *
     * @param userName userName
     * @return 用户角色集合
     */
    List<UmsRole> findUserRole(@Param("userName") String userName);

}
