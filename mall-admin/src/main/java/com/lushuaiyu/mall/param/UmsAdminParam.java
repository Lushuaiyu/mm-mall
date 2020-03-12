package com.lushuaiyu.mall.param;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 后台用户表
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Data
public class UmsAdminParam implements Serializable {


    private static final long serialVersionUID = -4342793278347500053L;
    private String id;

    private String username;

    private String password;

    /**
     * 头像
     */
    private String icon;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 备注信息
     */
    private String note;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 最后登录时间
     */
    private String loginTime;

    /**
     * 帐号启用状态：0->禁用；1->启用
     */
    private String status;

    /**
     * 是否删除 0 否 1 是
     */
    private String deleted;


}
