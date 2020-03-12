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
 * 后台用户角色和权限关系表
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Data
public class UmsRolePermissionRelationVo implements Serializable {


    private static final long serialVersionUID = 6633711935404352045L;
    private String id;

    private String roleId;

    private String permissionId;

    /**
     * 是否删除 0 否 1 是
     */
    private String deleted;


}
