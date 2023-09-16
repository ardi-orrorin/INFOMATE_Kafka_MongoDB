package com.infomate.chat.config;

import com.infomate.chat.dto.SessionDTO;
import com.infomate.chat.dto.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.chrono.Chronology;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class FilterChannelInterceptor implements ChannelInterceptor {

    @Value("${server.first.api-token}")
    private String FIRST_SERVER_API;

    @Value("${server.first.host}")
    private String FIRST_SERVER;

    private Flux<TokenDTO> tokenDTOList;
    private Mono<TokenDTO> tokenDTO;

    private List<SessionDTO> sessionDTOList = new ArrayList<>();


    public Mono<TokenDTO> userInfo(String jwt){
        return WebClient.create()
                .post().uri(FIRST_SERVER+"/api/v1/userinfo")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.WWW_AUTHENTICATE, FIRST_SERVER_API)
                .bodyValue("sdfdsf")
                .retrieve()
                .bodyToMono(TokenDTO.class);
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if(accessor.getCommand() == StompCommand.CONNECT){

            String jwt = accessor.getNativeHeader("Authorization").get(0);
            String sessionId = accessor.getSessionId();
            Mono<Integer> i = WebClient.create()
                    .get().uri(FIRST_SERVER+"/api/v1/userinfo")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .header(HttpHeaders.WWW_AUTHENTICATE, FIRST_SERVER_API)
                    .header("Authorization", "Bearer " + jwt)
                    .retrieve()
                    .bodyToMono(Integer.class);

            sessionDTOList.add(new SessionDTO(i.block(), sessionId, jwt));
//            log.info("[FilterChannelInterceptor](preSend) i : {} ",i.block());
//            log.info("[FilterChannelInterceptor](preSend) jwt : {} ",jwt);


            log.info("[FilterChannelInterceptor](StompCommand.CONNECT) sessionDTOList : {} ", sessionDTOList);

            //            StompHeaderAccessor 헤더 등록
        }

        if(accessor.getCommand() == StompCommand.SEND){


//            1. 유효기간 만료 확인
//            2. 만료 직전인 경우 jwt 재발생
//            3. 발송

        }

        if(accessor.getCommand() == StompCommand.DISCONNECT){
            String sessionId = accessor.getSessionId();
            sessionDTOList = sessionDTOList.stream().filter(sessionDTO ->
                !sessionDTO.getSessionId().equals(sessionId)
            ).collect(Collectors.toList());

            log.info("[FilterChannelInterceptor](StompCommand.DISCONNECT) sessionDTOList : {} ", sessionDTOList);
//
//            1. 유저 정보 리스트 삭제
//            tokenDTOList.filter(tokenDTO1 ->
//                            !tokenDTO1.getSessionId().equals(accessor.getSessionId()))
//                    .subscribe();

        }

        log.info("[FilterChannelInterceptor](preSend) :accessor : {} ",accessor);
        return message;
    }

}
