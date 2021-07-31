package com.reputation.orchestration.config;

import com.reputation.common.config.EmailClientConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.reputation.admin.client.AdminClientConfig;
import com.reputation.common.config.RabbitMqConfig;
import com.reputation.location.client.LocationClientConfig;
import org.springframework.web.client.RestTemplate;

/*
 * Application specific configuration to reference beans of required clients.
 */

@Import({ LocationClientConfig.class, EmailClientConfig.class, RestTemplate.class, AdminClientConfig.class,
        RabbitMqConfig.class })
@Configuration
public class OrchestrationServerConfig {

}
