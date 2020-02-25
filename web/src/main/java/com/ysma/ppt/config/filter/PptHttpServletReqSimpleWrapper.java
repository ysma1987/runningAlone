package com.ysma.ppt.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * ppt 定制httpServletRequest
 * Created by ysma on 2018/5/22.
 * 缺少对reader的重写
 * @see PptHttpServletRequestWrapper 完整版
 */
@Slf4j
public class PptHttpServletReqSimpleWrapper extends HttpServletRequestWrapper{

    private static final String ENCODING = "UTF-8";
    /**
     * Constructs a request object wrapping the given request.
     *
     * @throws IllegalArgumentException if the request is null
     */
    public PptHttpServletReqSimpleWrapper(HttpServletRequest request) throws IOException {
        super(request);
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        String paramStr = buffer.toString();
        byte[] originBytes = paramStr.getBytes();
        byte[] bytes;
        if (Base64.isBase64(originBytes)) {
            bytes = Base64.decodeBase64(originBytes);
        } else {
            bytes = originBytes;
        }
        log.info("requestlog 记录入参:{}", new String(bytes, ENCODING));
    }
}