package com.lushuaiyu.mall.param;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台用户权限表
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Data
public class UmsPermissionParam implements Serializable {


    private static final long serialVersionUID = -2117158849693551059L;
    private String id;

    /**
     * 父级权限id
     */
    private String pid;

    /**
     * 名称
     */
    private String name;

    /**
     * 权限值
     */
    private String value;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限类型：0->目录；1->菜单；2->按钮（接口绑定权限）
     */
    private String type;

    /**
     * 前端资源路径
     */
    private String uri;

    /**
     * 启用状态；0->禁用；1->启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 排序
     */
    private String sort;


}
