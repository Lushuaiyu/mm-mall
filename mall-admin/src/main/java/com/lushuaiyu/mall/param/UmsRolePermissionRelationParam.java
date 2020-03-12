package com.lushuaiyu.mall.param;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台用户角色和权限关系表
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Data
public class UmsRolePermissionRelationParam implements Serializable {


    private static final long serialVersionUID = 7701963016063227481L;
    private String id;

    private String roleId;

    private String permissionId;

    /**
     * 是否删除 0 否 1 是
     */
    private String deleted;


}
