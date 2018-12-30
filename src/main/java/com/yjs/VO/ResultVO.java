package com.yjs.VO;

import lombok.Data;

/**
 * Created by sjyjs on 2018/12/27.
 */
@Data
public class ResultVO<T> {

    private Integer code;

    private String msg;

    private T data;

}
