package com.yjs.VO;

import lombok.Data;

import java.io.Serializable;


@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 4465219625046762037L;

    private Integer code;

    private String msg;

    private T data;

}
