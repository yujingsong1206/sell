package com.yjs.enums;

import com.yjs.exception.SellException;
import lombok.Getter;

/**
 * 支付状态
 */
@Getter
public enum PayStatusEnum {

    NOPAY(0, "未支付"),
    PAY(1, "已支付"),
    ;

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static PayStatusEnum codeOf(Integer code){
        for (PayStatusEnum payStatusEnum : values()){
            if(payStatusEnum.getCode() == code){
                return payStatusEnum;
            }
        }
        throw new SellException(ResultEnum.ENUM_CODE_NOT_EXIST);
    }

}
