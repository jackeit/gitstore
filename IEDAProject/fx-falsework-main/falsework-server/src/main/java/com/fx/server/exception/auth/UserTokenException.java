package com.fx.server.exception.auth;


import com.fx.server.constant.CommonConstants;
import com.fx.server.exception.BaseException;

/**
 *
 * @Description:
 *
 * @param:
 * @return:
 * @auther: liwen
 * @date: 2020/8/2 10:57 上午
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, CommonConstants.EX_USER_INVALID_CODE);
    }
}
