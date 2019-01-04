package com.yjs.aspect;

import com.google.gson.Gson;
import com.yjs.dataobject.User;
import com.yjs.exception.SellerAuthorizeException;
import com.yjs.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 判断是否登录
 */
@Aspect
@Component
@Slf4j
public class LoginAuthorizeAspect {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

//    @Pointcut("execution(public * com.yjs.controller.Seller*.*(..))" +
//            "&& !execution(public * com.yjs.controller.UserController.*(..))")
    @Pointcut("execution(public * com.yjs.controller.Seller*.*(..))")
    public void verify(){
    }

    @Before("verify()")
    public void doVerify(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        String token = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(token)){
            log.warn("【登录校验】Cookie中查不到token");
            throw new SellerAuthorizeException();
        }
        String tokenValue = stringRedisTemplate.opsForValue().get(token);
        if(StringUtils.isEmpty(tokenValue)){
            log.warn("【登录校验】Redis中查不到token");
            throw new SellerAuthorizeException();
        }
    }

}
