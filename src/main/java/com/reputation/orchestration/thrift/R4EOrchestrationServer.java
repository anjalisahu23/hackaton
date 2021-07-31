package com.reputation.orchestration.thrift;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.reputation.emailer.client.ContentProvider;
import com.reputation.emailer.client.Email;
import com.reputation.emailer.client.EmailClient;
import com.reputation.emailer.client.Emailer;
import com.reputation.orchestration.delegate.ServiceDelegate;
import org.apache.thrift.TException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reputation.common.service.MiddlewareThriftService;
import com.reputation.orchestration.service.OrchestrationServer;
import com.reputation.thrift.r4e.common.RequestHeader;
import com.reputation.thrift.r4e.common.Response;
import com.reputation.thrift.r4e.common.ResponseCode;

@Service
public class R4EOrchestrationServer extends MiddlewareThriftService
        implements com.reputation.thrift.orchestration.R4EOrchestration.Iface {

    protected static final Logger logger = LoggerFactory.getLogger(R4EOrchestrationServer.class);

    private final OrchestrationServer orchestrationServer;

    private final EmailClient emailClient;

    @Autowired
    public R4EOrchestrationServer(OrchestrationServer orchestrationServer, EmailClient emailClient) {
        this.orchestrationServer = orchestrationServer;
        this.emailClient = emailClient;
    }

    @Override
    public Response publishReviewEvent(RequestHeader requestHeader, String eventType, String businessKey)
            throws TException {
        orchestrationServer.publishRabbitMQEvent(requestHeader, eventType, businessKey);
        return null;
    }

    @Override
    public Response emulate(RequestHeader requestHeader, int processes, int processTime) throws TException {
        List<String> businessKeys = new ArrayList<>();
        while (processes-- != 0) {
            String businessKey = UUID.randomUUID().toString();
            orchestrationServer.process(requestHeader, processTime, businessKey);
            businessKeys.add(businessKey);
        }
        Response response = new Response(ResponseCode.Success);
        response.setMessage("Business Keys: ".concat(String.join(", ", businessKeys)));
        return response;
    }

    @Override
    public Response onboardCustomer(RequestHeader requestHeader, String messageId, String businessKey)
            throws TException {
        orchestrationServer.onboardCustomer(requestHeader, messageId, businessKey);
        return null;
    }

    @Override
    public Response sendEmailForTicTic(String sender, List<String> recipients, String subject, String content)
            throws TException {

        Response response = new Response();
        try {
            Emailer emailer = new Emailer.Builder().emailClient(emailClient).from(sender)
                    .replyTo("asahu+test@reputation.com")
                    .addEmailTos(recipients)
                    .subject(subject).contentProvider(new ContentProvider() {
                        @Override
                        public Email email() {
                            Email email = new Email(content,
                                    subject, "tic tic team",
                                    "r4e-tic@reputation.com");
                            return email;
                        }
                    }).build();
            emailer.send();
            response.setMessage("Email sent successfully ");
        } catch (Exception e) {
            response.setMessage("Failed to send email");
            logger.error("sending email failed :{}", e);
        } finally {
        }
        return response;
    }

}
