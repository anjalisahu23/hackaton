package com.reputation.orchestration;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;

import com.reputation.common.spring.RdcApplication;
import com.reputation.orchestration.config.MainConfig;
import com.reputation.orchestration.config.OrchestrationServerConfig;

@EnableProcessApplication
public class Main {
    public static void main(String[] args) {
        RdcApplication.run(new SpringApplicationBuilder()
                .bannerMode(Banner.Mode.OFF)
                .sources(MainConfig.class, OrchestrationServerConfig.class), args);
    }
}
