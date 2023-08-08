package com.infomate.chat.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@ComponentScan(basePackages = "com.infomate.chat")
@EnableReactiveMongoRepositories(basePackages = "com.infomate.chat")
public class Config {

    @Bean
    public ModelMapper modelMapper(){return new ModelMapper();}
}
