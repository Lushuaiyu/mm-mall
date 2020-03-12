package com.lushuaiyu.mall.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lushuaiyu.mall.annotation.Pass;
import com.lushuaiyu.mall.api.ResponseHelper;
import com.lushuaiyu.mall.api.ResponseModel;
import com.lushuaiyu.mall.base.BusinessException;
import com.lushuaiyu.mall.param.UmsAdminParam;
import com.lushuaiyu.mall.service.UmsAdminService;
import com.lushuaiyu.mall.vo.UmsAdminVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import java.util.Deque;
import java.util.LinkedList;

/**
 * <p>
 * 后台用户表 前端控制器
 * </p>
 *
 * @author lushuaiyu
 * @since 2020-03-07
 */
@RestController
@SwaggerDefinition(tags = {
        @Tag(name = "UmsAdminController", description = "后台用户管理")

})
//@Api(tags = "UmsAdminController")
@RequestMapping("/ums-admin")
public class UmsAdminController {

    private static final String REHISTER_FAILED = "注册参数填写错误";

    @Resource
    private UmsAdminService umsAdminService;

    @PostMapping("register")
    @Pass
    public ResponseModel register(UmsAdminParam umsAdminParam) throws Exception {
        if (umsAdminParam == null) {
            throw new BusinessException(REHISTER_FAILED);
        }
        UmsAdminVo register = umsAdminService.register(umsAdminParam);
        return ResponseHelper.buildResponseModel(register);
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    @Pass
    public ResponseModel login(String username, String password) throws Exception {
        String token = umsAdminService.login(username, password);
        return ResponseHelper.buildResponseModel(token);
    }

    @ApiOperation(value = "获取当前用户的信息")
    @RequestMapping(value = "getCurrentUserInfo", method = RequestMethod.POST)
    public ResponseModel getCurrentUserInfo(ServletRequest request) {
        UmsAdminVo currentUserInfo = umsAdminService.getCurrentUserInfo(request);
        return ResponseHelper.buildResponseModel(currentUserInfo);
    }

    @ApiOperation(value = "登出功能")
    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseModel logout() {
        return ResponseHelper.buildResponseModel(null);
    }

    @ApiOperation(value = "根据用户名或姓名分页获取用户列表")
    @RequestMapping(value = "getUserList")
    public ResponseModel getUserList(String name, @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {
        Deque<Integer> d = new LinkedList<>();
        IPage<UmsAdminVo> userList = umsAdminService.getUserList(name, pageNum, pageSize);
        return ResponseHelper.buildResponseModel(userList);
    }

    @ApiOperation("获取指定用户信息")
    @RequestMapping(value = "getItem/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseModel getItem(@PathVariable Long id) {
        UmsAdminVo item = umsAdminService.getItem(id);
        return ResponseHelper.buildResponseModel(item);
    }

    @ApiOperation("修改指定用户信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ResponseModel update(UmsAdminParam admin) throws Exception {
        boolean count = umsAdminService.update(admin);
        return ResponseHelper.buildResponseModel(count);
    }




}
