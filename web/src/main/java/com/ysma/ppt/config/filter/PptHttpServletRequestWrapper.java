package com.ysma.ppt.config.filter;

import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ppt 定制httpServletRequest
 * Created by ysma on 2018/5/22.
 */
public class PptHttpServletRequestWrapper extends HttpServletRequestWrapper{

    private static final String ENCODING = "UTF-8";

    /**不重写 会导致request取不到数据*/
    private byte[] bytes;

    private String getQueryStr;

    /**
     * Constructs a request object wrapping the given request.
     *
     * @throws IllegalArgumentException if the request is null
     */
    public PptHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        String method = request.getMethod();
        if("GET".equals(method)){
            String queryStr = request.getQueryString();
            if(StringUtils.isNotBlank(queryStr)){
                if (Base64.isBase64(queryStr)) {
                    byte[] bytes = Base64.decodeBase64(queryStr);
                    getQueryStr = new String(bytes, ENCODING);
                } else {
                    getQueryStr = queryStr;
                }
            }
        } else {
            StringBuilder buffer = new StringBuilder();
            String line;
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            String paramStr = buffer.toString();
            byte[] originBytes = paramStr.getBytes();
            if (Base64.isBase64(originBytes)) {
                bytes = Base64.decodeBase64(originBytes);
            } else {
                bytes = originBytes;
            }
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream baInputStream = new ByteArrayInputStream(bytes);
        return new ServletInputStream()
        {
            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            public int read() {
                return baInputStream.read();
            }
        };
    }

    /**
     * The default behavior of this method is to return getReader()
     * on the wrapped request object.
     */
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    /**
     * @return get方法复写
     */
    @Override
    public String getQueryString() {
        return getQueryStr;
    }


}