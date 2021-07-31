package com.reputation.orchestration.event.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reputation.orchestration.service.OrchestrationService;
import com.reputation.thrift.r4e.acommon.event.SourceChangeEvent;
import com.reputation.thrift.r4e.lambda.event.LambdaObjectEvent;

import com.reputation.thrift.r4e.common.RequestHeader;

@Service
public class RabbitMQEventHandler {

    private final OrchestrationService orchestrationService;

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQEventHandler.class);

    @Autowired
    public RabbitMQEventHandler(OrchestrationService orchestrationService) {
        this.orchestrationService = orchestrationService;
    }

    public void handleEvent(LambdaObjectEvent event) {
        logger.info("Handle event processing here");
        logger.info("Event received", event);
    }

    public void handleEvent(SourceChangeEvent event) {
        logger.info("Handle event processing here");
        RequestHeader requestHeader = new RequestHeader();
        orchestrationService.startProcess(requestHeader, "Review Collected");
    }
}
