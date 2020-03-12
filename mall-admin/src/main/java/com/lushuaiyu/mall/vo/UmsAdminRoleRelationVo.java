package com.lushuaiyu.mall.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class UmsAdminRoleRelationVo implements Serializable {

    private static final long serialVersionUID = 7579963319866491536L;
    private String id;

    private String adminId;

    private String roleId;

    /**
     * 是否删除 0 否 1 是
     */
    private String deleted;


}
