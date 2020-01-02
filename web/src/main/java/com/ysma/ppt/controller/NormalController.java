package com.ysma.ppt.controller;

import com.ysma.ppt.PptFuture;
import com.ysma.ppt.intf.Json;
import com.ysma.ppt.service.NormalService;
import com.ysma.ppt.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ysma
 * 常规controller
 */
@Slf4j
@RestController
@RequestMapping(value = "/normal")
public class NormalController {

    @Autowired
    private NormalService normalService;

    /**
     * 1.@RequestBody 注解 application/json请求模式  可演示415参数媒体格式错误
     *
     * 2.ThreadLocal 演示内存泄漏 注意需修改server.tomcat.max-threads的配置为1
     */
    @RequestMapping(value = "/api")
    public String api(@RequestBody PptFuture pptFuture){

        log.debug("经过一番测算,name:{} age:{}的未来可期啊",
                pptFuture.getName(),
                pptFuture.getAge());
        ThreadUtil.setString("age:" + pptFuture.getAge());
        String result = normalService.ask(pptFuture.getName());
        //ThreadUtil.clear(); //TODO 内存泄漏的源泉
        return result;
    }

    /**
     * 2、@Json 自定义参数解析
     *    2-1、HttpParamFilter 拦截请求
     *    2-2、PptHttpServletReqSimpleWrapper 包装request不覆写getReader getInputStream
     *    2-3、PptHttpServletRequestWrapper 包装request覆写getReader getInputStream
     *
     *  演示如何前置拦截request参数解析 可推到后置处理return 返回值
     *
     *  演示解密解密
     *
     *  演示inputStream流 不可重复读  by MarkSupport方法默认false不支持重复读
     *      BufferedInputStream等缓冲流才具有可重复读的功能
     *      注意http.wrap.flag配置
     */
    @RequestMapping(value = "/api2")
    public String api2(@Json PptFuture pptFuture){

        log.debug("经过一番测算,name:{} age:{}的未来可期啊",
                pptFuture.getName(),
                pptFuture.getAge());
        return normalService.ask(pptFuture.getName());
    }

}
