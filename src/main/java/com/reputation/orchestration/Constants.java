package com.reputation.orchestration;

public interface Constants {

    static final String SERVICE_NAME = "r4e-orchestration";

    static final String EVENT_EXCHANGE = "r4e.orchestration";

    static final String COLLECT_QUEUE_BINDING = "collect.event.inbound";

    static final String MERU_QUEUE_BINDING = "meru.event.inbound";

    static final String FAILED_RESPONSE_QUEUE_BINDING = "failed.response.event.inbound";

    static final String PUBLISH_RESPONSE_QUEUE_BINDING = "publish.response.event.inbound";

    static final String REPBIZ_QUEUE_BINDING = "repbiz.event.inbound";

    static final String TICKET_QUEUE_BINDING = "ticket.event.inbound";

    static final String INTERNAL_ACTION_QUEUE_BINDING = "internal.action.event.inbound";

    static final String LAMBDA_QUEUE_BINDING = "lambda.event.inbound";

    static final String DEFAULT_ROUTING = "#";

}
