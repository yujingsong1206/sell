package com.yjs.enums;

import com.yjs.exception.SellException;
import lombok.Getter;

/**
 * 订单状态
 */
@Getter
public enum OrderStatusEnum {

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

    public static OrderStatusEnum codeOf(Integer code){
        for (OrderStatusEnum orderStatusEnum : values()){
            if(orderStatusEnum.getCode() == code){
                return orderStatusEnum;
            }
        }
        throw new SellException(ResultEnum.ENUM_CODE_NOT_EXIST);
    }

}
