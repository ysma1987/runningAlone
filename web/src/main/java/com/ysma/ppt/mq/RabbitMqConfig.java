package com.ysma.ppt.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnExpression("${server.mode.rabbitmq.consumer:false}")
public class RabbitMqConfig {

    @Value("${rabbitmq.queue.name.request}")
    private String requestQueueName;

    @Value("${rabbitmq.queue.name.response}")
    private String responseQueueName;


    @Bean
    public String responseQueueName() {
        return responseQueueName;
    }

    @Bean
    public Queue requestQueue() {
        return new Queue(requestQueueName);
    }

    @Bean
    public Queue responseQueue() {
        return new Queue(responseQueueName);
    }
}
