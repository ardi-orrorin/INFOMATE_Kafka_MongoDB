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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Configuration
public class FilterChannelInterceptor implements ChannelInterceptor {

    @Value("${server.first.api-token}")
    private String FIRST_SERVER_API;

    @Value("${server.first.host}")
    private String FIRST_SERVER;

    private final RestTemplate restTemplate;

    private final List<TokenDTO> tokenDTOList;

    public FilterChannelInterceptor(RestTemplate restTemplate, List<TokenDTO> tokenDTOList) {
        this.restTemplate = restTemplate;
        this.tokenDTOList = tokenDTOList;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        Optional<TokenDTO> tokenDTO = null;


        if(accessor.getCommand() == StompCommand.CONNECT){

//            1. api 호출 유저 정보 확인
//            2. tokenDTO 객체 리스트 추가
//            3. 사용불가능한 토큰 exception 처리


//            tokenDTO = restTemplate
//                    .exchange(RequestEntity.post("").body(""), TokenDTO.class)
//                    .getBody();

            log.info("[FilterChannelInterceptor](preSend) :tokenDTO : {} ",tokenDTO);

            //            StompHeaderAccessor 헤더 등록
        }

        if(accessor.getCommand() == StompCommand.SEND){
            tokenDTO = tokenDTOList.stream()
                     .filter(e-> e.getJwt().equals(accessor.getNativeHeader("")))
                     .findFirst();

//            1. 유효기간 만료 확인
//            2. 만료 직전인 경우 jwt 재발생
//            3. 발송

        }

        if(accessor.getCommand() == StompCommand.DISCONNECT){

//            1. 유저 정보 리스트 삭제

        }

        log.info("[FilterChannelInterceptor](preSend) :accessor : {} ",accessor);
        return message;
    }
}
