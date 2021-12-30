package com.dandan.crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatUtil {
    /**
     * 对Date对象进行格式化操作 "yyyy-MM-dd HH-mm-ss"
     * @param date
     * @return
     */
    public static String getDateFormatTime(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        return sf.format(date);
    }
    /**
     * 对Date对象进行格式化操作 "yyyy-MM-dd"
     * @param date
     * @return
     */
    public static String getFormatDate(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        return sf.format(date);
    }
    /**
     * 对Date对象进行格式化操作 "HH-mm-ss"
     * @param date
     * @return
     */
    public static String getFormatTime(Date date){
        SimpleDateFormat sf = new SimpleDateFormat("HH-mm-ss");
        return sf.format(date);
    }
}
