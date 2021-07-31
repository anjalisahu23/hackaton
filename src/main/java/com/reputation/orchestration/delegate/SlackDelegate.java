package com.reputation.orchestration.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.reputation.orchestration.domain.SlackMessage;
import com.reputation.orchestration.util.SlackUtil;

@Service("slackAdapter")
public class SlackDelegate implements JavaDelegate {

    protected static final Logger logger = LoggerFactory.getLogger(SlackDelegate.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        SlackMessage slackMessage = new SlackMessage();
        slackMessage.setUsername("orchestrationUser");
        if (execution.getProcessDefinitionId().contains("SocialListingStep1")) {
            if (execution.getCurrentActivityName().equals("Esclate")) {
                slackMessage.setText("Delay in collecting customer data for onboarding social listing");
            } else {
                slackMessage.setText("Collecting Assension (91485) data  for onboarding Social Listing is completed");
            }
        } else if (execution.getProcessDefinitionId().contains("SocialListingStep2")) {
            slackMessage.setText(
                    "Building query for Ascension(91485) Social Listing onboarding and query validation completed");
        } else if (execution.getProcessDefinitionId().contains("SocialListingStep3")) {
            slackMessage.setText("Query result verified  and handed over to TSE team to implement in production");
        } else if (execution.getProcessDefinitionId().contains("SocialListingStep4")) {
            slackMessage.setText(
                    "professional services ticket created  to complete onboarding Process of Assension(91485)");
        }
        SlackUtil.sendMessage(slackMessage);

    }

}
