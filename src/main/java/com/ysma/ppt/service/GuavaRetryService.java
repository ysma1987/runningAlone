package com.ysma.ppt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GuavaRetryService {

    public String retry(String param){
        if(!"java".equals(param)){
            log.error("===============error param:{}", param);
            throw new IllegalArgumentException("test");
        }
        return param + ":good";
    }
}
