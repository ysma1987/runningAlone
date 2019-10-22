package com.ysma.ppt.ppt.util.time;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * es时间为utc时间 此为转换类
 */
@Slf4j
public class TimeDiyUtil {

    private static final String MODE = "yyyy-MM-dd HH:mm:ss";

    //本地时间转utc时间 下同
    public static String transformToUtcTime(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(MODE);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(date);
    }

    public static String transformToUtcTime(String date){
        SimpleDateFormat cst = new SimpleDateFormat(MODE);
        SimpleDateFormat sdf = new SimpleDateFormat(MODE);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.format(cst.parse(date));
        } catch (ParseException e) {
            log.error("TimeDiyUtil.transformToUtcTime 字符串转日期异常，date:{}", date, e);
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(transformToUtcTime("2019-08-21 20:58:13"));
    }
}
