package com.yjs.enums;

import com.yjs.exception.SellException;
import lombok.Getter;

/**
 * 支付状态
 */
@Getter
public enum PayStatusEnum implements CodeEnum {

    NOPAY(0, "未支付"),
    PAY(1, "已支付"),
    ;

    private Integer code;
    private String msg;

    PayStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
