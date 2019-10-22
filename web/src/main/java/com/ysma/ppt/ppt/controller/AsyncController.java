package com.ysma.ppt.ppt.controller;

import com.ysma.ppt.ppt.PptFuture;
import com.ysma.ppt.ppt.service.async.FutureService;
import com.ysma.ppt.ppt.service.async.ThreadService;
import com.ysma.ppt.ppt.util.ThreadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@RestController
@RequestMapping(value = "/async")
public class AsyncController {

    @Autowired
    private FutureService futureService;

    @Autowired
    private ThreadService threadService;

    /**
     * 1.@Async注解 演示异步功能 编程趋向简单
     *
     * 2.演示 InheritableThreadLocal 的父子线程共享变量问题
     *
     * 3.演示CompletableFuture 的异步管理功能
     */
    @RequestMapping(value = "/api")
    public String api(@RequestBody PptFuture pptFuture) throws ExecutionException, InterruptedException {

        log.debug("经过一番测算,name:{} age:{}的未来可期啊",
                pptFuture.getName(),
                pptFuture.getAge());

        ThreadUtil.setString("age:" + pptFuture.getAge());
        ThreadUtil.setInheritString("age:" + pptFuture.getAge());

        CompletableFuture first = CompletableFuture.anyOf(
                futureService.guess(pptFuture.getName()),
                futureService.guessAgain(pptFuture.getName())
        );
        //保持良好的编码习惯 clear ThreadLocal的内容
        ThreadUtil.clear();
        return first.get().toString();
    }
}
