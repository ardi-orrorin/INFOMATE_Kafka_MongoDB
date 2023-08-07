package com.infomate.chat.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean
    public Executor asyncThreadPool(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(80);
        taskExecutor.setQueueCapacity(80);
        taskExecutor.setThreadNamePrefix("Async");
        taskExecutor.setDaemon(true);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
