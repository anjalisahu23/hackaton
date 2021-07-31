package com.reputation.orchestration.domain;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.reputation.core.Idable;
import com.reputation.dao.annotations.CreatedDate;
import com.reputation.dao.annotations.UpdatedDate;

/*
 * Domain object for mongo collection
 */

@Document(
        collection = "events_received")
public class OrchestrationEventsCollection implements Idable<String> {
    public static final String FIELD_ID = "id";
    public static final String FIELD_EVENT_TYPE = "et";
    public static final String FIELD_RECEIVED_DATE = "rd";
    public static final String FIELD_CREATED_DATE = "cdt";
    public static final String FIELD_UPATED_DATE = "udt";

    @Id
    private String id;

    @Field(FIELD_EVENT_TYPE)
    private String eventType;

    @Field(FIELD_RECEIVED_DATE)
    private Date receivedDate;

    @CreatedDate
    @Field(FIELD_CREATED_DATE)
    private Date createdDate;

    @UpdatedDate
    @Field(FIELD_UPATED_DATE)
    private Date updatedDate;

    public String getId() {
        return id;
    }

    public OrchestrationEventsCollection setId(String id) {
        this.id = id;
        return this;
    }

    public OrchestrationEventsCollection setEventType(String eventType) {
        this.eventType = eventType;
        return this;
    }

    public OrchestrationEventsCollection setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
        return this;
    }

    public OrchestrationEventsCollection setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public OrchestrationEventsCollection setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public String getEventType() {
        return eventType;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }
}
