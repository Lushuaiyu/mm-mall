package com.lushuaiyu.mall.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lushuaiyu.mall.entity.UmsAdmin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lushuaiyu.mall.param.UmsAdminParam;
import com.lushuaiyu.mall.vo.UmsAdminVo;

import javax.servlet.ServletRequest;

/**
 * <p>
 * 后台用户表 服务类
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
public interface UmsAdminService extends IService<UmsAdmin> {

    /**
     * 用户注册
     * @param umsAdminParam
     * @throws Exception 异常
     * @return
     */
    UmsAdminVo register(UmsAdminParam umsAdminParam) throws Exception;


    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return token
     */
    String login(String username, String password) throws Exception;

    /**
     * 获取当前用户的登录信息
     * @param request
     * @return
     */
    UmsAdminVo getCurrentUserInfo(ServletRequest request);

    /**
     * 根据用户名或者昵称分页查询用户
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<UmsAdminVo> getUserList(String name, Integer pageNum, Integer pageSize);

    /**
     * 获取指定用户信息
     * @param id
     * @return
     */
   UmsAdminVo getItem(Long id);


    /**
     * 修改指定用户信息
     * @param entity
     * @throws Exception
     * @return
     */
    boolean update(UmsAdminParam entity) throws Exception;
}
