package com.ysma.ppt.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * @author ysma
 * AES加解密工具 from webconsole
 */
@Slf4j
public class AESTool {
    public static final String DEFAULT_KEY = "1559038590713859";
    public static final String CHAR_ENCODING = "UTF-8";
    public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";

    public AESTool() {
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(1, seckey);
            return cipher.doFinal(data);
        } catch (Exception var7) {
            log.error("AESTool.encrypt Exception", var7);
            throw new RuntimeException("encrypt fail!", var7);
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec seckey = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(2, seckey);
            return cipher.doFinal(data);
        } catch (Exception var6) {
            log.error("AESTool.decrypt Exception", var6);
            throw new RuntimeException("decrypt fail!", var6);
        }
    }

    public static String encryptToBase64(String data, String key) {
        try {
            byte[] valueByte = encrypt(data.getBytes(CHAR_ENCODING), key.getBytes(CHAR_ENCODING));
            return new String(Base64.getEncoder().encode(valueByte));
        } catch (UnsupportedEncodingException var5) {
            log.error("AESTool.encryptToBase64 UnsupportedEncodingException", var5);
            throw new RuntimeException("encryptToBase64 fail!", var5);
        }
    }

    public static String decryptFromBase64(String data, String key) {
        try {
            byte[] originalData = Base64.getDecoder().decode(data.getBytes());
            byte[] valueByte = decrypt(originalData, key.getBytes(CHAR_ENCODING));
            return new String(valueByte, CHAR_ENCODING);
        } catch (UnsupportedEncodingException var4) {
            log.error("AESTool.decryptFromBase64 UnsupportedEncodingException", var4);
            throw new RuntimeException("decrypt fail!", var4);
        }
    }

    public static String encryptToBase64(String data){
        try {
            return new String(Base64.getEncoder().encode(data.getBytes()), CHAR_ENCODING);
        } catch (UnsupportedEncodingException var3) {
            log.error("AESTool.decryptFromBase64 UnsupportedEncodingException", var3);
            throw new RuntimeException("encryptToBase64 fail!", var3);
        }
    }

    public static String decryptFromBase64(String data) {
        try {
            byte[] originalData = Base64.getDecoder().decode(data.getBytes());
            return new String(originalData, CHAR_ENCODING);
        } catch (UnsupportedEncodingException var2) {
            log.error("AESTool.decryptFromBase64 UnsupportedEncodingException", var2);
            throw new RuntimeException("UnsupportedEncodingException fail!", var2);
        }
    }

    public static void main(String[] args) {
        String password =encryptToBase64("Hell0Hadp", DEFAULT_KEY);
        System.out.println(password);
        String origin = decryptFromBase64(password, DEFAULT_KEY);
        System.out.println(origin);
    }
}