package com.yjs.handler;

import com.yjs.VO.ResultVO;
import com.yjs.config.ProjectUrlConfig;
import com.yjs.exception.SellException;
import com.yjs.exception.SellerAuthorizeException;
import com.yjs.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class SellExceptionHandler {

    @Autowired
    private ProjectUrlConfig projectUrlConfig;

    //拦截登录异常
    @ExceptionHandler(value = SellerAuthorizeException.class)
    public ModelAndView handlerSellerAuthorizeException(){
        return new ModelAndView("redirect:" + projectUrlConfig.getSell() + "/sell/user/login");
    }

    @ExceptionHandler(value = SellException.class)
    @ResponseBody
//    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultVO handlerSellException(SellException e){
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }

}
