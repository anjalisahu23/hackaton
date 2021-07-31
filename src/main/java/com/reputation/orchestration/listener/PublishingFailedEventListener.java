package com.reputation.orchestration.listener;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
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
import com.reputation.orchestration.Constants;
import com.reputation.orchestration.event.handler.RabbitMQEventHandler;
import com.reputation.thrift.orchestration.event.ProcessFlowEvent;

@Component
public class PublishingFailedEventListener {

    protected static final Logger logger = LoggerFactory.getLogger(PublishingFailedEventListener.class);

    private RabbitMQEventHandler rabbitMQEventHandler;

    public static final String EVENT_TYPE = "responsePublishingFailedEvent";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    PublishingFailedEventListener(RabbitMQEventHandler rabbitMQEventHandler) {
        this.rabbitMQEventHandler = rabbitMQEventHandler;
    }

    @Bean
    Queue failedResponseEventQueue() {
        return new Queue(Constants.FAILED_RESPONSE_QUEUE_BINDING, true);
    }

    @Bean
    TopicExchange failedResponseEventExchange() {
        return new TopicExchange(Constants.EVENT_EXCHANGE, true, false);
    }

    @Bean
    Binding failedResponseQueueBinding(@Qualifier("failedResponseEventQueue") Queue queue,
            @Qualifier("failedResponseEventExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.DEFAULT_ROUTING);
    }

    @RabbitListener(
            containerFactory = RabbitMqConfig.FACTORY,
            queues = { Constants.FAILED_RESPONSE_QUEUE_BINDING })
    public void onInboundEvent(ProcessFlowEvent event) {
        try {
            logger.info("Event received: {}", event);

            if (event.getEventType().equalsIgnoreCase(EVENT_TYPE)) {
                System.out.println("BUSINESSKEY: " + event.getBusinessKey());
                MessageCorrelationResult result = runtimeService.createMessageCorrelation(event.getEventType())
                        .processInstanceBusinessKey(event.getBusinessKey()).setVariable("Hello", "World")
                        .correlateWithResult();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
