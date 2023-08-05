package com.infomate.chat.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableReactiveMongoRepositories(basePackages = "com.infomate.chat")
public class AsyncConfig {

    @Bean
    public Executor asyncThreadPool(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(800);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("Async");
        taskExecutor.setDaemon(true);
        taskExecutor.initialize();
        return taskExecutor;
    }
}
