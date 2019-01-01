package com.yjs.utils;


import com.yjs.enums.CodeEnum;

public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass){
        for (T each: enumClass.getEnumConstants()){
            if(code == each.getCode()){
                return each;
            }
        }
        return null;
    }

}
