package com.ysma.ppt.ppt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

/**
 * spring提供的重试服务
 *
 * 可Junit测试
 */
@Slf4j
@Service
public class SpringRetryService {

    @Retryable
    public String retry(String param){

        if(!"java".equals(param)){
            log.error("===============error param:{}", param);
            throw new IllegalArgumentException("test");
        }
        return param + ":good";

    }
}
