package com.yjs.exception;

import com.yjs.enums.ResultEnum;

/**
 * Created by sjyjs on 2018/12/28.
 */
public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum){
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

}