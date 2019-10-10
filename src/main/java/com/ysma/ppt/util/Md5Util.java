package com.ysma.ppt.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author ysma md5工具
 */
@Slf4j
public class Md5Util {

    public static void main(String[] args) {
        String a = "{\"ysma\":\"dx\",\"hello\":\"world\"}";
        String b = hexBit32(a);
        System.out.println(b);
        String c = hexBit16(b);
        System.out.println(c);
    }

    public static byte[] digest(String data){
        try {
            // 拿到一个MD5转换器
            MessageDigest messageDigest =MessageDigest.getInstance("MD5");

            // 输入的字符串转换成字节数组
            byte[] inputByteArray = data.getBytes();

            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);

            // 转换并返回结果，也是字节数组，包含16个元素
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("Md5Util.stringMD5 exception", e);
            return null;
        }
    }

    public static String hexBit32(String data) {
        // 字符数组转换成字符串返回
        return Hex.encodeHexString(Objects.requireNonNull(digest(data)), false);
    }

    public static String hexBit16(String data) {
        // 字符数组转换成字符串返回
        return hexBit32(data).substring(8, 24);
    }
}
