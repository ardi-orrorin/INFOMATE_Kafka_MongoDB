package com.infomate.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerAdapter;
//

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
public class ChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatApplication.class, args);
    }


}
