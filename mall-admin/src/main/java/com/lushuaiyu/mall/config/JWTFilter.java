package com.lushuaiyu.mall.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lushuaiyu.mall.api.ResponseModel;
import com.lushuaiyu.mall.api.ResponseResource;
import com.lushuaiyu.mall.base.Constant;
import com.lushuaiyu.mall.base.PublicResultConstant;
import com.lushuaiyu.mall.entity.UmsAdmin;
import com.lushuaiyu.mall.service.RedisService;
import com.lushuaiyu.mall.service.UmsAdminService;
import com.lushuaiyu.mall.util.ComUtil;
import com.lushuaiyu.mall.util.JWTUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author grm
 * <p>
 * 代码的执行流程preHandle->isAccessAllowed->isLoginAttempt->executeLogin
 */
public class JWTFilter extends BasicHttpAuthenticationFilter {

    @Resource
    private UmsAdminService userService;

    @Resource
    private RedisService redisService;

    @Value("${jwt-redis.key}")
    private String jwtRedis;

    /**
     * 过期时间2天
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60 * 2;
    private static final long EXPIRE_ONE_TIME = 24 * 60 * 60;

    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    /**
     *
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader("Authorization");
        JWTToken token = new JWTToken(authorization);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        getSubject(request, response).login(token);
        // 如果没有抛出异常则代表登入成功，返回true
        setUserBean(request, response, token);
        return true;
    }

    /**
     * 刷新 token
     *
     * @param request
     * @param response
     * @return
     */
    private boolean refreshToken(ServletRequest request, ServletResponse response) {
        UmsAdmin currentUser = (UmsAdmin) request.getAttribute("currentUser");

        if (!org.springframework.util.StringUtils.isEmpty(currentUser)) {
            try {
                String key = (String) redisService.get(jwtRedis + currentUser.getUsername());
                if (redisService.hasKey(key)) {

                    // key 剩余时间, 单位: 秒
                    Long ttl = redisService.getExpire(jwtRedis + currentUser.getUsername());

                    // 如果 redis 里的 token 剩余时间 > 当前时间 + 1 天 && < 当前时间两天后的时间 , 则刷新新的 token, 否则 抛异常
                    if (ttl > EXPIRE_ONE_TIME && ttl < EXPIRE_TIME) {
                        String refreshToken = JWTUtil.sign(currentUser.getUsername(), currentUser.getPassword());
                        // 把新的 token 设置到 redis 里
                        redisService.set(jwtRedis + currentUser.getUsername(), refreshToken, EXPIRE_TIME);

                        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                        httpServletResponse.setHeader("Authorization", refreshToken);
                        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
                        String authorization = httpServletRequest.getHeader("Authorization");

                        JWTToken token1 = new JWTToken(authorization);

                        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
                        getSubject(request, response).login(token1);
                        // 如果没有抛出异常则代表登入成功，返回true
                        setUserBean(request, response, token1);


                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }


        return false;
    }


    /**
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (isLoginAttempt(request, response)) {
            try {
                executeLogin(request, response);
            } catch (Exception e) {
                String msg = e.getMessage();
                Throwable throwable = e.getCause();
                if (throwable != null && throwable instanceof SignatureVerificationException) {
                    msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                } else if (throwable != null && throwable instanceof TokenExpiredException) {
                    // AccessToken已过期
                    if (refreshToken(request, response)) {
                        return true;
                    } else {
                        msg = "Token已过期(" + throwable.getMessage() + ")";
                    }
                } else {
                    if (throwable != null) {
                        msg = throwable.getMessage();
                    }
                }
                this.response401(request, response, msg);
                return false;
            }
        }
        return true;
    }

    private void setUserBean(ServletRequest request, ServletResponse response, JWTToken token) {
        if (this.userService == null) {
            this.userService = SpringContextBean.getBean(UmsAdminService.class);
        }
        String username = JWTUtil.getUsername(token.getPrincipal().toString());
        QueryWrapper<UmsAdmin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        UmsAdmin userBean = userService.list(queryWrapper).stream().findFirst().get();
        request.setAttribute("currentUser", userBean);

    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        String authorization = httpServletRequest.getHeader("Authorization");
        if (verificationPassAnnotation(request, response, httpServletRequest, authorization)) {
            return true;
        }
        if (ComUtil.isEmpty(authorization)) {
            responseError(request, response);
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 验证请求方法是否有@Pass注解,有则直接放行
     *
     * @param request
     * @param response
     * @param httpServletRequest
     * @param authorization
     * @return
     * @throws Exception
     */
    private boolean verificationPassAnnotation(ServletRequest request, ServletResponse response, HttpServletRequest httpServletRequest, String authorization) throws Exception {
        for (String urlMethod : Constant.METHOD_URL_SET) {
            String[] split = urlMethod.split(":--:");
            if (split[0].equals(httpServletRequest.getRequestURI())
                    && (split[1].equals(httpServletRequest.getMethod()) || split[1].equals("RequestMapping"))) {
                Constant.isPass = true;
                if (ComUtil.isEmpty(authorization)) {
                    //如果当前url不需要认证，则注入当前登录用户时，给一个空的
                    httpServletRequest.setAttribute("currentUser", new UmsAdmin());
                    return true;
                } else {
                    super.preHandle(request, response);
                }
            }
            if (StringUtils.countMatches(urlMethod, "{") > 0 &&
                    StringUtils.countMatches(urlMethod, "/") == StringUtils.countMatches(split[0], "/")
                    && (split[1].equals(httpServletRequest.getMethod()) || split[1].equals("RequestMapping"))) {
                if (isSameUrl(split[0], httpServletRequest.getRequestURI())) {
                    Constant.isPass = true;
                    if (ComUtil.isEmpty(authorization)) {
                        httpServletRequest.setAttribute("currentUser", new UmsAdmin());
                        return true;
                    } else {
                        super.preHandle(request, response);
                    }
                }
            }
        }
        return false;
    }

    /**
     * 判断路径参数的url是否和controller方法url一致
     *
     * @param localUrl
     * @param requestUrl
     * @return
     */
    private boolean isSameUrl(String localUrl, String requestUrl) {
        String[] tempLocalUrls = localUrl.split("/");
        String[] tempRequestUrls = requestUrl.split("/");
        if (tempLocalUrls.length != tempRequestUrls.length) {
            return false;
        }
        StringBuilder sbLocalUrl = new StringBuilder();
        StringBuilder sbRequestUrl = new StringBuilder();
        for (int i = 0; i < tempLocalUrls.length; i++) {
            if (StringUtils.countMatches(tempLocalUrls[i], "{") > 0) {
                tempLocalUrls[i] = "*";
                tempRequestUrls[i] = "*";
            }
            sbLocalUrl.append(tempLocalUrls[i] + "/");
            sbRequestUrl.append(tempRequestUrls[i] + "/");
        }
        return sbLocalUrl.toString().trim().equals(sbRequestUrl.toString().trim());
    }

    /**
     * 非法url返回身份错误信息
     */
    private void responseError(ServletRequest request, ServletResponse response) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("utf-8");
            out = response.getWriter();
            response.setContentType("application/json; charset=utf-8");
            out.print(JSONObject.toJSONString(ResponseResource.validationFailure(PublicResultConstant.UNAUTHORIZED)));
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 401非法请求
     *
     * @param req
     * @param resp
     */
    private void response401(ServletRequest req, ServletResponse resp, String msg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();

            ResponseModel result = new ResponseModel();
            result.setResult(false);
            result.setCode("-1");
            result.setMessage(msg);
            out.append(JSON.toJSONString(result));
        } catch (IOException e) {
            LoggerFactory.getLogger("返回Response信息出现IOException异常:" + e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
