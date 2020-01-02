package com.ysma.ppt.util.compress;

import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

/**
 * @author ysma 超类 抽象共性行为
 */
@Slf4j
public abstract class AbstCompress implements ICompress{

    private static final String EN_CODING = "utf-8";

    /**压缩*/
    public String compress(String dataMsg){

        try {
            //1.base64编码 二进制转义
            byte[] base64Str = Base64.getEncoder().encode(dataMsg.getBytes(EN_CODING));

            //2.zip压缩
            byte[] compressBytes = compress(base64Str);

            //3.获取并转义压缩内容
            return Base64.getEncoder().encodeToString(compressBytes);
        } catch (Exception e) {
            log.error("GzipCompress.compress 压缩异常", e);
            throw new RuntimeException("GzipCompress.compress 压缩异常", e);
        }
    }

    /**解压缩*/
    public String unCompress(String dataMsg){

        try {
            //1.转义二进制
            byte[] base64Bytes = Base64.getDecoder().decode(dataMsg.getBytes(EN_CODING));

            //1.zip解压缩
            byte[] unCompressBytes = uncompress(base64Bytes);

            //3.base64 二进制恢复
            byte[] originMsg = Base64.getDecoder().decode(unCompressBytes);
            return new String(originMsg);
        } catch (Exception e) {
            log.error("GzipCompress.unCompress 解压缩异常", e);
            throw new RuntimeException("GzipCompress.unCompress 解压缩异常", e);
        }
    }
}
