package com.lushuaiyu.mall.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lushuaiyu.mall.base.BusinessException;
import com.lushuaiyu.mall.base.PublicResultConstant;
import com.lushuaiyu.mall.entity.UmsAdmin;
import com.lushuaiyu.mall.mapper.UmsAdminMapper;
import com.lushuaiyu.mall.param.UmsAdminParam;
import com.lushuaiyu.mall.service.UmsAdminService;
import com.lushuaiyu.mall.util.JWTUtil;
import com.lushuaiyu.mall.vo.UmsAdminVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 后台用户表 服务实现类
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@Service
public class UmsAdminServiceImpl extends ServiceImpl<UmsAdminMapper, UmsAdmin> implements UmsAdminService {

    @Resource
    private UmsAdminMapper umsAdminMapper;


    @Override
    public boolean update(UmsAdminParam entity) throws Exception {
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtil.copyProperties(entity, umsAdmin, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        boolean count = this.updateById(umsAdmin);
        if (count) {
            return true;
        } else {
            throw new BusinessException(PublicResultConstant.ERROR);
        }

    }

    @Override
    public UmsAdminVo getItem(Long id) {
        UmsAdmin getUserItemByUserId = this.getById(id);
        UmsAdminVo adminVo = new UmsAdminVo();
        BeanUtil.copyProperties(getUserItemByUserId, adminVo,
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setIgnoreError(true));
        return adminVo;
    }

    @Override
    public IPage<UmsAdminVo> getUserList(String name, Integer pageNum, Integer pageSize) {
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtils.isNotBlank(name), "username", name)
                .or()
                .like(StringUtils.isNotBlank(name), "nick_name", name)
                .orderByDesc("create_time").select(UmsAdmin.class, info -> !info.getColumn().equals("deleted") &&
                !info.getColumn().equals("note"));
        Page<UmsAdmin> page = new Page<>(pageNum, pageSize);
        Page<UmsAdmin> adminPage = umsAdminMapper.selectPage(page, queryWrapper);
        IPage<UmsAdminVo> adminVoIPage = adminPage.convert(x -> {
            UmsAdminVo adminVo = new UmsAdminVo();
            BeanUtil.copyProperties(x, adminVo, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
            return adminVo;
        });
        return adminVoIPage;
    }

    @Override
    public UmsAdminVo getCurrentUserInfo(ServletRequest request) {
        List<List<Integer>> ans = new LinkedList<>();
        ans.add(0, new ArrayList<>());
        UmsAdmin currentUser = (UmsAdmin) request.getAttribute("currentUser");
        UmsAdminVo adminVo = new UmsAdminVo();
        BeanUtil.copyProperties(currentUser, adminVo,
                CopyOptions.create()
                        .setIgnoreNullValue(true));
        return adminVo;
    }

    @Override
    public String login(String username, String password) throws Exception {

        //根据用户名查询用户
        QueryWrapper<UmsAdmin> umsAdminQueryWrapper = new QueryWrapper<>();
        umsAdminQueryWrapper.eq("username", username);
        List<UmsAdmin> umsAdmins = umsAdminMapper.selectList(umsAdminQueryWrapper);
        if (umsAdmins.size() <= 0) {
            throw new BusinessException(PublicResultConstant.INVALID_USER);
        }

        UmsAdmin user = umsAdmins.stream()
                .findFirst()
                .get();

        // 如果客户端输入的密码为空 && 根据 username 查出来的集合 size != 1 && 注册和登录时输入的密码不一样
        if (StringUtils.isBlank(password) && umsAdmins.size() != 1 && !BCrypt.checkpw(password, user.getPassword())) {
            throw new BusinessException(PublicResultConstant.INVALID_USERNAME_PASSWORD);
        }

        String token = JWTUtil.sign(user.getUsername(), user.getPassword());
        return token;
    }

    @Override
    public UmsAdminVo register(UmsAdminParam umsAdminParam) throws Exception {
        // 参数拷贝
        UmsAdmin umsAdmin = new UmsAdmin();
        BeanUtil.copyProperties(umsAdminParam, umsAdmin,
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setIgnoreError(true));

        // 查询是否有相同用户名的用户
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        String userName = Optional.ofNullable(umsAdmin)
                .map(UmsAdmin::getUsername)
                .orElse(StringUtils.EMPTY);
        String passWord = Optional.ofNullable(umsAdmin)
                .map(UmsAdmin::getPassword)
                .orElse(StringUtils.EMPTY);
        queryWrapper.eq("username", userName);
        List<UmsAdmin> list = umsAdminMapper.selectList(queryWrapper);
        if (list.size() > 0) {
            throw new BusinessException(PublicResultConstant.INVALID_USER_EXIST);
        }
        umsAdmin.setStatus(1);
        umsAdmin.setCreateTime(LocalDateTime.now());
        //密码加密
        umsAdmin.setPassword(BCrypt.hashpw(passWord, BCrypt.gensalt()));
        umsAdminMapper.insert(umsAdmin);
        UmsAdminVo adminVo = new UmsAdminVo();
        BeanUtil.copyProperties(umsAdmin, adminVo,
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setIgnoreError(true));
        return adminVo;
    }
}
