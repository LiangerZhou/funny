package com.liang.funny.controller;

import com.liang.funny.util.Json.JsonUtil;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 统一捕捉shiro的异常，返回给前台一个json信息，前台根据这个信息显示对应的提示，或者做页面的跳转。
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    //不满足@RequiresGuest注解时抛出的异常信息
    private static final String GUEST_ONLY = "Attempting to perform a guest-only operation";


    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public JsonUtil handleShiroException(ShiroException e) {
        String eName = e.getClass().getSimpleName();
        log.error("shiro执行出错：{}",eName);
        return new JsonUtil(eName, false, Codes.SHIRO_ERR, "鉴权或授权过程出错", null);
    }

    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public JsonUtil page401(UnauthenticatedException e) {
        String eMsg = e.getMessage();
        if (StringUtils.startsWithIgnoreCase(eMsg,GUEST_ONLY)){
            return new JsonUtil("401", false, Codes.UNAUTHEN, "只允许游客访问，若您已登录，请先退出登录", null)
                    .data("detail",e.getMessage());
        }else{
            return new JsonUtil("401", false, Codes.UNAUTHEN, "用户未登录", null)
                    .data("detail",e.getMessage());
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public JsonUtil page403() {
        return new JsonUtil("403", false, Codes.UNAUTHZ, "用户没有访问权限", null);
    }

}