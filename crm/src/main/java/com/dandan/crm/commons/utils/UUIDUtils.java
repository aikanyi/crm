package com.dandan.crm.commons.utils;

import java.util.UUID;
//生成一段32位的字符串
public class UUIDUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
