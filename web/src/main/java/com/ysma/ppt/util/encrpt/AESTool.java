package com.ysma.ppt.util.encrpt;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;

/**
 * @author mays
 * AES加解密工具 from webconsole
 */
@Slf4j
public class AESTool {

    public static final String DEFAULT_KEY = "1559038590713859";

    public static final String CHAR_ENCODING = "UTF-8";

    public static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    public static final int ENCRYPTION = 1;

    public static final int DECRYPTION = 2;

    private static Cipher initCipher(byte[] key, int opMode) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(opMode, secretKeySpec);
            return cipher;
        } catch (Exception ex) {
            log.error("AESTool.initCipher Exception", ex);
            throw new RuntimeException("AESTool.initCipher init fail!", ex);
        }
    }

    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            Cipher cipher = initCipher(key, ENCRYPTION);
            return cipher.doFinal(data);
        } catch (Exception var7) {
            log.error("AESTool.encrypt Exception", var7);
            throw new RuntimeException("encrypt fail!", var7);
        }
    }

    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            Cipher cipher = initCipher(key, DECRYPTION);
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

    public static String encryptToBase64(String data) {
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

    public static String aesKeyGen() {
        StringBuilder uid = new StringBuilder();
        //产生16位的强随机数
        Random rd = new SecureRandom();
        for (int i = 0; i < 16; i++) {
            //产生0-2的3位随机数
            int type = rd.nextInt(3);
            switch (type){
                case 0:
                    //0-9的随机数
                    uid.append(rd.nextInt(10));
                    break;
                case 1:
                    //ASCII在65-90之间为大写,获取大写随机
                    uid.append((char)(rd.nextInt(25)+65));
                    break;
                case 2:
                    //ASCII在97-122之间为小写，获取小写随机
                    uid.append((char)(rd.nextInt(25)+97));
                    break;
                default:
                    break;
            }
        }
        return uid.toString();
    }

    public static void main(String[] args) {
        String key = aesKeyGen();
        System.out.print(key + "\n");
        String password = encryptToBase64("Hell0Hadp", key);
        System.out.print(password + "\n");
        String origin = decryptFromBase64(password, key);
        System.out.print(origin);
    }
}
