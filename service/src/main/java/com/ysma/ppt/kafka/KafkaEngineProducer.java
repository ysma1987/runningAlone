package com.ysma.ppt.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * desc: TODO
 *
 * @author ysma
 * date : 2020/4/20 19:11
 */
@Slf4j
@Component
public class KafkaEngineProducer {

    private  KafkaTemplate<Object, Object> template;

    public KafkaEngineProducer(KafkaTemplate template) {
        this.template = template;
    }

    public void send(String topic, Object key, Object val){
        ListenableFuture<SendResult<Object, Object>> future = template.send(topic, key, val);
        future.addCallback(ok-> log.info("消息发送成功:{}", ok), error->log.error("error", error));
    }
}
