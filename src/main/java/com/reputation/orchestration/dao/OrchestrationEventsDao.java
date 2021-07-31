package com.reputation.orchestration.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.reputation.dao.mongo.AbstractDao;
import com.reputation.dao.mongo.MongoTemplates;
import com.reputation.orchestration.domain.OrchestrationEventsCollection;

@Component
public class OrchestrationEventsDao extends AbstractDao<OrchestrationEventsCollection, String> {

    @Autowired
    public OrchestrationEventsDao(MongoTemplates mongoTemplates) {
        super(mongoTemplates);
    }
}
