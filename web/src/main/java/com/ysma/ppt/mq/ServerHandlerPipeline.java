package com.ysma.ppt.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServerHandlerPipeline {

    void execute(Object message) {
        log.info("receive msg:{}", message);
    }

}
