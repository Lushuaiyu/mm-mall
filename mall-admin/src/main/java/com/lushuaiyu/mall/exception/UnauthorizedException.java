package com.lushuaiyu.mall.exception;

/**
 * 身份认证异常
 * @author lsy
 * @since 2018-05-06
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
