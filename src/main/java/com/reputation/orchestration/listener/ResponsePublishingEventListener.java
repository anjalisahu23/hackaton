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
public class ResponsePublishingEventListener {

    protected static final Logger logger = LoggerFactory.getLogger(ResponsePublishingEventListener.class);

    private RabbitMQEventHandler rabbitMQEventHandler;

    public static final String EVENT_TYPE_RESPONSE_FAILED = "responseFailed";

    public static final String EVENT_TYPE_RESPONSE_PUBLISHED = "responsePublished";

    public static final String EVENT_TYPE_INVALID_CREDENTIAL = "invalidCredential";

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    ResponsePublishingEventListener(RabbitMQEventHandler rabbitMQEventHandler) {
        this.rabbitMQEventHandler = rabbitMQEventHandler;
    }

    @Bean
    Queue publishResponseEventQueue() {
        return new Queue(Constants.PUBLISH_RESPONSE_QUEUE_BINDING, true);
    }

    @Bean
    TopicExchange publishResponseEventExchange() {
        return new TopicExchange(Constants.EVENT_EXCHANGE, true, false);
    }

    @Bean
    Binding publishResponseQueueBinding(@Qualifier("publishResponseEventQueue") Queue queue,
            @Qualifier("publishResponseEventExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.DEFAULT_ROUTING);
    }

    @RabbitListener(
            containerFactory = RabbitMqConfig.FACTORY,
            queues = { Constants.PUBLISH_RESPONSE_QUEUE_BINDING })
    public void onInboundEvent(ProcessFlowEvent event) {
        try {
            logger.info("Event received: {}", event);

            if (event.getEventType().equalsIgnoreCase(EVENT_TYPE_RESPONSE_FAILED)
                    || event.getEventType().equalsIgnoreCase(EVENT_TYPE_RESPONSE_PUBLISHED)
                    || event.getEventType().equalsIgnoreCase(EVENT_TYPE_INVALID_CREDENTIAL)) {
                MessageCorrelationResult result = runtimeService.createMessageCorrelation(event.getEventType())
                        .processInstanceBusinessKey(event.getBusinessKey()).setVariable("Hello", "World")
                        .correlateWithResult();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
