package com.infomate.chat.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@Slf4j
@Configuration
public class FilterChannelInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//        if(accessor.getCommand() == StompCommand.CONNECT){
//            accessor.setNativeHeader("X-Authorization", accessor.getNativeHeader("X-Authorization").get(0));
//        }
        log.info("[FilterChannelInterceptor](preSend) :accessor : {} ",accessor);
        return message;
    }
}
