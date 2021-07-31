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
public class R4ECollectEventListener {

    protected static final Logger logger = LoggerFactory.getLogger(R4ECollectEventListener.class);

    private RabbitMQEventHandler rabbitMQEventHandler;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    R4ECollectEventListener(RabbitMQEventHandler rabbitMQEventHandler) {
        this.rabbitMQEventHandler = rabbitMQEventHandler;
    }

    @Bean
    Queue collectEventQueue() {
        return new Queue(Constants.COLLECT_QUEUE_BINDING, true);
    }

    @Bean
    TopicExchange collectEventExchange() {
        return new TopicExchange(Constants.EVENT_EXCHANGE, true, false);
    }

    @Bean
    Binding collectQueueBinding(@Qualifier("collectEventQueue") Queue queue,
            @Qualifier("collectEventExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(Constants.DEFAULT_ROUTING);
    }

    @RabbitListener(
            containerFactory = RabbitMqConfig.FACTORY,
            queues = { Constants.COLLECT_QUEUE_BINDING })
    public void onInboundEvent(ProcessFlowEvent event) {
        try {
            logger.info("Event received: {}", event);
            if (event.getEventType().equalsIgnoreCase("CollectReview")) {
                MessageCorrelationResult result = runtimeService.createMessageCorrelation(event.getEventType())
                        .processInstanceBusinessKey(event.getBusinessKey())
                        .setVariable("Hello", "World")
                        .correlateWithResult();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}
