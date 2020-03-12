package com.lushuaiyu.mall.param;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台用户和角色关系表
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Data
public class UmsAdminRoleRelationParam implements Serializable {

    private static final long serialVersionUID = -4736659470634755431L;
    private String id;

    private String adminId;

    private String roleId;

    /**
     * 是否删除 0 否 1 是
     */
    private String deleted;


}
