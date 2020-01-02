package com.ysma.ppt.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author guangya.zhao @Date: 2019/8/13 16:42
 */
@Slf4j
@Component
public class ServerHandlerPipeline {

    void execute(Object message) {
        log.info("receive msg:{}", message);
    }

}
