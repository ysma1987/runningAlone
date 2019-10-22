package com.ysma.ppt.ppt.config.filter;

import com.ysma.ppt.ppt.util.resource.VariableConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * http请求 入参 reader流重复读取实现
 * Created by ysma on 2018/5/22.
 *
 * filterName的首字母一定要小写！！！小写！！！小写！！！
 */
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "httpParamFilter")
@Order(1)
public class HttpParamFilter implements Filter {

    @Autowired
    private VariableConfig variableConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("======HttpParamFilter init==========");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        if(variableConfig.getHttpWrapFlag()){
            filterChain.doFilter(new PptHttpServletRequestWrapper(req), resp);
        } else {
            filterChain.doFilter(new PptHttpServletReqSimpleWrapper(req), resp);
        }

    }

    @Override
    public void destroy() {
        log.debug("======HttpParamFilter destroyed==========");
    }
}