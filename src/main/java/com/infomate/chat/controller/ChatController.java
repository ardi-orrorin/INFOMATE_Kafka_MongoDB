package com.infomate.chat.controller;

import com.infomate.chat.dto.ChatDTO;
import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Chat;
import com.infomate.chat.service.ChatService;
import com.infomate.chat.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.StompSubProtocolHandler;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;

@RestController
@RequiredArgsConstructor
@Slf4j
@EnableMongoAuditing
@EnableAsync
public class ChatController {

    private final ChatService chatService;

    private final KafkaTemplate<String , MessageDTO> kafkaTemplate;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @Async(value = "asyncThreadPool")
    @EventListener(SessionConnectEvent.class)
    public void webSocketConnect(SessionConnectEvent event){

//        log.info("[ChatController](webSocketConnect) principal : {}", principal);
        log.info("[ChatController](webSocketConnect) event : {}", event);

        // 계정 정보 받은 후 정보 반응
    }

    @Async(value = "asyncThreadPool")
    @EventListener(SessionDisconnectEvent.class)
    public void webSocketDisConnect(SessionDisconnectEvent event){

        log.info("[ChatController](webSocketDisConnect) event : {}", event);
    }

    @Async(value = "asyncThreadPool")
    @MessageMapping("/chat/{receiver}")
    public void subScriber(@DestinationVariable Integer receiver, MessageDTO message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        log.info("[ChatController](subScriber) simpMessageHeaderAccessor : {}", simpMessageHeaderAccessor);
        log.info("[ChatController](subScriber) receiver : {}", receiver);
        log.info("[ChatController](subScriber) message : {}", message);

        message.setCreateDate(LocalDateTime.now());
        message.setHeader(simpMessageHeaderAccessor.getFirstNativeHeader("X-Authorization"));
//        message.setCreateDate(LocalDateTime.now()); @CreateDate 테스트

        Mono.just(message).subscribe(messageDTO -> kafkaTemplate.send("topic01", messageDTO).whenComplete(
                new BiConsumer<SendResult<String, MessageDTO>, Throwable>() {
                    @Override
                    public void accept(SendResult<String, MessageDTO> stringMessageDTOSendResult, Throwable throwable) {
                        log.info("[ChatController](subScriber) message : {}", message);
                        log.info("[ChatController](subScriber) 메세지 전송 성공");

                        chatService.insertMessage(messageDTO); // 작업 안함
                    }
                })
        );
    }

    @Async(value = "asyncThreadPool")
    @KafkaListener(topics = "topic01", groupId = "foo")
    public void publisher(@Payload MessageDTO message, SimpMessageHeaderAccessor accessor){

        log.info("[ChatController](publisher) receiver : {}", message);
        log.info("[ChatController](publisher) accessor : {}", accessor);

        message.getReceiveList().forEach(member -> {
                    log.info("[ChatController](publisher)  member : {}", member);
                    if (member != message.getSender()) {
                        simpMessageSendingOperations.convertAndSend("/sub/chat/" + member, message);
                    }
                }
        );
    }
    
    @GetMapping("/chat/{roomNo}/{memberCode}/{day}")
    public Flux<Chat> findAllMessage(@PathVariable Integer roomNo,
                                     @PathVariable Integer memberCode,
                                     @PathVariable LocalDate day){

        log.info("[ChatController](findAllMessage) : roomNo : {} ", roomNo);
        log.info("[ChatController](findAllMessage) : memberCode : {} ", memberCode);
        log.info("[ChatController](findAllMessage) : day : {} ", day);

//        return chatService.findMessageByDay(Mono.just(roomNo), Mono.just(memberCode), Mono.just(day));

        return Flux.just(ChatDTO.builder()
                .chatRoomNo(roomNo)
                .sender(memberCode)
                .createDate(day.atStartOfDay())
                .build()).flatMap(e-> chatService.findMessageByDay(e));
    }



    @GetMapping("/chat/room/{roomId}")
    public ResponseEntity<ResponseDTO> findAllMessageByRoomAndCreateDate(@PathVariable Integer roomId)  {
        return ResponseEntity.ok()
                .body(ResponseDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("success")
//                        .data(chatService.findAllMessageByRoom(roomId).get())
                        .build());
    }
}
