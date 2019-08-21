package com.ysma.ppt.controller;

import com.ysma.ppt.PptFuture;
import com.ysma.ppt.intf.Json;
import com.ysma.ppt.service.NormalService;
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
     * 1.普通请求 application/json请求模式
     * 415参数媒体格式错误  大概率出现此问题
     * @param pptFuture 入参载体
     * @return resp
     */
    @RequestMapping(value = "/api")
    public String api(@RequestBody PptFuture pptFuture){

        log.info("经过一番测算,name:{} age:{}的未来可期啊",
                pptFuture.getName(),
                pptFuture.getAge());
        pptFuture.setDestiny("good");
        return "good";
    }

    /**
     * 2、不指定application/json 怎么办？
     * @param pptFuture 入参载体
     * @return resp
     */
    @RequestMapping(value = "/api2")
    public String api2(@Json PptFuture pptFuture){

        log.info("经过一番测算,name:{} age:{}的未来可期啊",
                pptFuture.getName(),
                pptFuture.getAge());
        pptFuture.setDestiny("good");
        return "good";
    }

    /**
     * 4、入参base64加密怎么办？
     * @param pptFuture 入参载体
     * @return resp
     */
    @RequestMapping(value = "/api3")
    public String api3(@Json PptFuture pptFuture){
        log.info("经过一番测算,name:{} age:{}的未来可期啊",
                pptFuture.getName(),
                pptFuture.getAge());
        pptFuture.setDestiny("good");
        return "good";
    }

}
