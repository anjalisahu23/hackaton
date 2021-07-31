package com.reputation.orchestration.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.reputation.common.config.RabbitMqConfig;
import com.reputation.orchestration.event.handler.RabbitMQEventHandler;
import com.reputation.thrift.r4e.lambda.event.LambdaObjectEvent;

@Component
public class RabbitMQEventListener {

    protected static final Logger logger = LoggerFactory.getLogger(RabbitMQEventListener.class);

    private RabbitMQEventHandler rabbitMQEventHandler;

    @Autowired
    RabbitMQEventListener(RabbitMQEventHandler rabbitMQEventHandler) {
        this.rabbitMQEventHandler = rabbitMQEventHandler;
    }

    @Bean
    Queue voidEventQueue() {
        return new Queue("r4e.lambda.lambdaobject.ticket.internal", true);
    }

    @Bean
    TopicExchange voidEventExchange() {
        return new TopicExchange("r4e.lambda.lambdaobject", true, false);
    }

    @Bean
    Binding voidQueueBinding(@Qualifier("voidEventQueue") Queue queue,
            @Qualifier("voidEventExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("ticketInternal");
    }

    @RabbitListener(
            containerFactory = RabbitMqConfig.FACTORY,
            queues = { "r4e.lambda.lambdaobject.ticket.internal" })
    public void onInboundEvent(LambdaObjectEvent event) {
        logger.info("Event received: {}", event);
        rabbitMQEventHandler.handleEvent(event);
    }
}
