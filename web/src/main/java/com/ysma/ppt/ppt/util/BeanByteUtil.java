package com.ysma.ppt.ppt.util;

import com.alibaba.fastjson.JSON;

import java.nio.charset.StandardCharsets;

/**
 * @author guangya.zhao @Date: 2019/8/15 11:26
 */
public class BeanByteUtil {

    public static byte[] toByte(Object bean) {
        return JSON.toJSONString(bean).getBytes(StandardCharsets.UTF_8);
    }

    public static <T> T toBean(byte[] bytes, Class<T> clazz) {
        Object parse = JSON.parse(new String(bytes, StandardCharsets.UTF_8));
        return JSON.parseObject(JSON.toJSONString(parse), clazz);
    }
}
