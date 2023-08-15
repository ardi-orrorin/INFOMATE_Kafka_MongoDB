package com.infomate.chat.config;

import com.infomate.chat.dto.TokenDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.RequestEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Configuration
public class FilterChannelInterceptor implements ChannelInterceptor {

    @Value("${server.first.api-token}")
    private String FIRST_SERVER_API;

    @Value("${server.first.host}")
    private String FIRST_SERVER;

    private final RestTemplate restTemplate;

    public FilterChannelInterceptor(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if(accessor.getCommand() == StompCommand.CONNECT){

            restTemplate.exchange(RequestEntity.post("").body(""), TokenDTO.class);
            //            StompHeaderAccessor 헤더 등록
        }
        log.info("[FilterChannelInterceptor](preSend) :accessor : {} ",accessor);
        return message;
    }
}
