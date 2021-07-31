package com.reputation.orchestration.config;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.reputation.dao.mongo.spring.DefaultMongoConfig;
import com.reputation.web.config.ManagementServerPropertiesConfig;

// import platform bean definitions required to build service
// MainConfig aggregated beans from DefaultMongoConfig and ManagementServerPropertiesConfig

@Configuration
@Import({ DefaultMongoConfig.class, ManagementServerPropertiesConfig.class })
@EnableAutoConfiguration(
        exclude = {
                MongoDataAutoConfiguration.class
        })
@ComponentScan(
        basePackages = { "com.reputation.orchestration.*" })
@EnableAsync
public class MainConfig {

    @Bean
    public Executor emulatorPool(@Value("${emulator.corePoolSize}") int corePoolSize,
            @Value("${emulator.maxPoolSize}") int maxPoolSize, @Value("${emulator.queueCapacity}") int queueCapacity) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("emulator-");
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }

}
