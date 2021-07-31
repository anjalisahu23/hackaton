package com.reputation.orchestration.service;

import com.reputation.thrift.r4e.common.RequestHeader;

public interface OrchestrationService {

    void startProcess(RequestHeader requestHeader, String messageName);
}
