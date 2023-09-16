package com.infomate.chat.config;


import com.infomate.chat.dto.SessionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@RequiredArgsConstructor
public class webSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final FilterChannelInterceptor filterChannelInterceptor;

    private final List<SessionDTO> sessionList;

    private final RestTemplate restTemplate;


    @EventListener(SessionConnectEvent.class)
    public void webSocketConnect(SessionConnectEvent event){
        log.info("[webSocketConfig](webSocketConnect) event : {}", event);

//        log.info("[webSocketConfig](webSocketConnect) i : {}", i);

//        userInfo("sd").subscribe().isDisposed();
//        log.info("[ChatController](webSocketConnect) principal : {}", principal);
        log.info("[ChatController](webSocketConnect) event : {}", event);

        // 계정 정보 받은 후 정보 반응
    }


    @EventListener(SessionDisconnectEvent.class)
    public void webSocketDisConnect(SessionDisconnectEvent event){

        log.info("[ChatController](webSocketDisConnect) event : {}", event);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        log.info("[webSocketConfig](configureClientInboundChannel) registration : {}", registration);
        registration.interceptors(filterChannelInterceptor);
    }
}
