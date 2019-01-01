package com.yjs.enums;

import com.yjs.exception.SellException;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
public enum OrderStatusEnum implements CodeEnum {

    NEW(0, "新订单"),
    FINISHED(1, "完成"),
    CANCEL(2, "取消"),
    ;

    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
