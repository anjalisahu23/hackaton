package com.reputation.orchestration.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reputation.emailer.client.ContentProvider;
import com.reputation.emailer.client.Email;
import com.reputation.emailer.client.EmailClient;
import com.reputation.emailer.client.Emailer;

@Service("emailAdapter")
public class ServiceDelegate implements JavaDelegate {

    protected static final Logger logger = LoggerFactory.getLogger(ServiceDelegate.class);
    private final EmailClient emailClient;

    public ServiceDelegate(EmailClient emailClient) {
        this.emailClient = emailClient;
    }

    // We can integrate email service here
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("Notification form LoggerDelegate");
        // Send mail by using email service

        try {
            Emailer emailer = new Emailer.Builder().emailClient(emailClient).from("asahu@reputation.com")
                    .replyTo("asahu+test@reputation.com")
                    .addEmailTos("asahu+test@reputation.com", "rgundimeda+test@reputation.com",
                            "pmishra+test@reputation.com", "pmutcharla+test@reputation.com",
                            "amaurya+test@reputation.com", "jannapolla+test@reputation.com")
                    .subject("r4e-orchestration Social On-boarding alert !").contentProvider(new ContentProvider() {
                        @Override
                        public Email email() {
                            Email email = new Email("Social On boarding not completed within specified time.",
                                    "Social On-boarding alert !", "Orchestration team",
                                    "r4e-orchestration@reputation.com");
                            return email;
                        }
                    }).build();
            emailer.send();
        } catch (Exception e) {
            logger.error("sending email failed :{}", e);
        } finally {
        }

    }
}
