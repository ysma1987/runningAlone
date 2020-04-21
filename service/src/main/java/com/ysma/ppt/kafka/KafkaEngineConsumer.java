package com.ysma.ppt.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

/**
 * desc: TODO
 *
 * @author ysma
 * date : 2020/4/20 19:27
 */
@Slf4j
@Component
public class KafkaEngineConsumer {

    @KafkaListener(id = "ctu-consumer-1",
            topics = "${spring.kafka.template.default-topic}",
            groupId = "${spring.kafka.consumer.groupId}")
    public void onMessage(ConsumerRecord<String, String> record,
                          Acknowledgment ack){
        log.info("topic={}, key={},val={}", record.topic(), record.key(), record.value());
        ack.acknowledge();
    }
}
