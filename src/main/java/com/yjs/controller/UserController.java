package com.yjs.controller;

import com.google.gson.Gson;
import com.yjs.config.ProjectUrlConfig;
import com.yjs.constant.RedisConstant;
import com.yjs.dataobject.User;
import com.yjs.enums.ResultEnum;
import com.yjs.service.UserService;
import com.yjs.utils.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    @GetMapping("/login")
    public ModelAndView loginBefore(){
        return new ModelAndView("/user/login");
    }

    @PostMapping("/login")
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpServletResponse response,
                              Map<String, Object> map){
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            map.put("msg", ResultEnum.UN_PW_NO_EMPTY.getMsg());
            map.put("url", "/sell/user/login");
            return new ModelAndView("/common/error", map);
        }
        User user = userService.findByUsernameAndPassword(username, password);
        if(user == null){
            map.put("msg", ResultEnum.LOGIN_FAIL.getMsg());
            map.put("url", "/sell/user/login");
            return new ModelAndView("/common/error", map);
        }

        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        Gson gson = new Gson();
        user.setPassword(null);
        stringRedisTemplate.opsForValue().set(token, gson.toJson(user), expire, TimeUnit.SECONDS);
        CookieUtil.writeLoginToken(response, token);
        return new ModelAndView("redirect:"+ projectUrlConfig.getSell() +"/sell/seller/order/list");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Map<String, Object> map){
        String token = CookieUtil.readLoginToken(request);
        if(!StringUtils.isEmpty(token)){
            stringRedisTemplate.opsForValue().getOperations().delete(token);
            CookieUtil.delLoginToken(request, response);
        }
        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMsg());
        map.put("url", projectUrlConfig.getSell() +"/sell/user/login");
        return new ModelAndView("/common/success", map);
    }

}
