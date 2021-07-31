package com.reputation.orchestration.service;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.reputation.common.utils.RandomUtils;
import com.reputation.orchestration.Constants;
import com.reputation.orchestration.listener.R4EInternalActionsEventListener;
import com.reputation.orchestration.listener.R4ELambdaEventListener;
import com.reputation.orchestration.listener.R4EMeruEventListener;
import com.reputation.orchestration.listener.R4ERepBizEventListener;
import com.reputation.orchestration.listener.R4ETicketEventListener;
import com.reputation.orchestration.listener.ResponsePublishingEventListener;
import com.reputation.thrift.orchestration.event.ProcessFlowEvent;
import com.reputation.thrift.r4e.common.EventHeader;
import com.reputation.thrift.r4e.common.RequestHeader;

/*
 * Application service provider.
 *
 * Implementation of Business logic goes here.
 *
 */

@Service
public class OrchestrationServer {

    protected static final Logger logger = LoggerFactory.getLogger(OrchestrationServer.class);

    private final OrchestrationService orchestrationService;

    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    public OrchestrationServer(OrchestrationService orchestrationService, RabbitTemplate rabbitTemplate) {

        this.orchestrationService = orchestrationService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishRabbitMQEvent(RequestHeader requestHeader, String eventType, String businessKey) {

        EventHeader eventHeader = new EventHeader();
        eventHeader.setAgencyID(requestHeader.getAgencyID());
        eventHeader.setTenantID(requestHeader.getTenantID());

        ProcessFlowEvent processFlowEvent = new ProcessFlowEvent();
        processFlowEvent.setEventType(eventType);
        processFlowEvent.setEventHeader(eventHeader);
        processFlowEvent.setBusinessKey(businessKey);
        rabbitTemplate.convertAndSend(Constants.EVENT_EXCHANGE,
                Constants.DEFAULT_ROUTING,
                processFlowEvent);

        logger.info("Event published: {}", processFlowEvent);
    }

    public void onboardCustomer(RequestHeader requestHeader, String message, String businessKey) {
        MessageCorrelationResult result = runtimeService.createMessageCorrelation(message)
                .processInstanceBusinessKey(businessKey)
                .setVariable("Hello", "World")
                .correlateWithResult();
    }

    @Async("emulatorPool")
    public void process(RequestHeader requestHeader, int processTime, String businessKey) {
        long delay = processTime * 1000;
        int fate = RandomUtils.randomInt(1, 100);
        try {
            publishRabbitMQEvent(requestHeader, R4ERepBizEventListener.EVENT_TYPE, businessKey);

            if (fate > 9 && fate < 11) {
                return;
            }

            Thread.sleep(delay);
            publishRabbitMQEvent(requestHeader, R4ELambdaEventListener.EVENT_TYPE, businessKey);

            if (fate > 23 && fate < 25) {
                return;
            }

            Thread.sleep(delay);
            publishRabbitMQEvent(requestHeader, R4ETicketEventListener.EVENT_TYPE, businessKey);

            if (fate > 39 && fate <= 41) {
                return;
            }

            Thread.sleep(delay);
            publishRabbitMQEvent(requestHeader, R4EInternalActionsEventListener.EVENT_TYPE,
                    businessKey);

            Thread.sleep(delay);
            publishRabbitMQEvent(requestHeader, R4EMeruEventListener.EVENT_TYPE, businessKey);

            Thread.sleep(delay);
            if (fate > 43 && fate <= 98) {
                // Thread.sleep(delay);
                publishRabbitMQEvent(requestHeader, ResponsePublishingEventListener.EVENT_TYPE_RESPONSE_PUBLISHED,
                        businessKey);
            } else if (fate > 98 && fate <= 99) {
                // Thread.sleep(delay);
                publishRabbitMQEvent(requestHeader, ResponsePublishingEventListener.EVENT_TYPE_RESPONSE_FAILED,
                        businessKey);
            } else if (fate > 99) {
                // Thread.sleep(delay);
                publishRabbitMQEvent(requestHeader, ResponsePublishingEventListener.EVENT_TYPE_INVALID_CREDENTIAL,
                        businessKey);
            }

        } catch (InterruptedException e) {
            logger.error("Error occurred while emulating the process.", e);
        }
    }

}
