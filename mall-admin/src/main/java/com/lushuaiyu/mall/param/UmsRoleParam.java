package com.lushuaiyu.mall.param;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台用户角色表
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Data
public class UmsRoleParam implements Serializable {


    private static final long serialVersionUID = 6746235468277636824L;
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 后台用户数量
     */
    private String adminCount;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 启用状态：0->禁用；1->启用
     */
    private String status;

    private String sort;

    /**
     * 是否删除 0 否 1 是
     */
    private String deleted;


}
