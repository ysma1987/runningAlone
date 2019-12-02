package com.ysma.ppt.ppt.mq;

import com.rabbitmq.client.Channel;
import com.ysma.ppt.ppt.util.BeanByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author guangya.zhao @Date: 2019/8/19 17:35
 */
@Slf4j
@Component
@RabbitListener(queues = "${rabbitmq.queue.name.request}")
@ConditionalOnExpression("${server.mode.rabbitmq.consumer:false}")
public class RabbitMqConsumer {

    @Autowired
    private ServerHandlerPipeline serverHandlerPipeline;

    @SendTo("#{responseQueueName}")//SpEL
    @RabbitHandler
    public byte[] received(byte[] request, Channel channel, Message msg) throws IOException {
        channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
        Object ctuRequest = BeanByteUtil.toBean(request, Object.class);
        serverHandlerPipeline.execute(ctuRequest);
        return BeanByteUtil.toByte("success");
    }
}
