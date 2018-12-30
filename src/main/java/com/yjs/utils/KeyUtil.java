package com.yjs.utils;


import java.util.Random;

public class KeyUtil {

    /**
     * 生成唯一的主键
     * 格式：时间 + 随机数（6位）
     */
    public static synchronized String genUniqueKey(){
        Random random = new Random();
        Integer randomNumber = random.nextInt(900000) + 100000;
        return System.currentTimeMillis() + String.valueOf(randomNumber);
    }

}
