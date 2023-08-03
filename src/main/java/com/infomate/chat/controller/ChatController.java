package com.infomate.chat.controller;

import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.service.ChatService;
import com.infomate.chat.common.ResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final KafkaTemplate<String , MessageDTO> kafkaTemplate;
    private final NewTopic myTopic1;


    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ChatService chatService;

    @Async(value = "asyncThreadPool")
    @EventListener
    public void webSocketConnect(SessionConnectEvent event){
        System.out.println("event = " + event);
    }

    @Async(value = "asyncThreadPool")
    @EventListener
    public void webSocketDisConnect(SessionDisconnectEvent event){
        System.out.println("event = " + event);
    }

    @Async(value = "asyncThreadPool")
    @MessageMapping("/chat/{receiver}")
    public void subScriber(@DestinationVariable Integer receiver, MessageDTO message){
        log.info("[ChatController](subScriber) receiver : {}", receiver);
        log.info("[ChatController](subScriber) message : {}", message);

        message.setCreateDate(LocalDateTime.now());
        CompletableFuture<SendResult<String, MessageDTO>> future =
                kafkaTemplate.send(myTopic1.name(), message);
        future.whenComplete(new BiConsumer<SendResult<String, MessageDTO>, Throwable>() {
            @Override
            public void accept(SendResult<String, MessageDTO> stringMessageDTOSendResult, Throwable throwable) {
                log.info("[ChatController](subScriber) message : {}", message);
                log.info("[ChatController](subScriber) 메세지 전송 성공");

                chatService.insertMessage(message);
            }
        });

    }

    @Async(value = "asyncThreadPool")
    @KafkaListener(topics = "topic01", groupId = "foo")
    public void publisher(@Payload MessageDTO message){
        log.info("[ChatController](publisher) receiver : {}", message);
        message.getReceiveList().forEach(member -> {
                    log.info("[ChatController](publisher)  member : {}",member);
                    if (member != message.getSender()) {
                        simpMessageSendingOperations.convertAndSend("/sub/chat/" + member, message);
                    }
                }
        );
    }


    @Async(value = "asyncThreadPool")
    @GetMapping("/chat/{userId}")
    public ResponseEntity<ResponseDTO> findAllMessage(@PathVariable Integer userId){

        return ResponseEntity.ok()
                .body(ResponseDTO.builder()
                                .statusCode(HttpStatus.OK.value())
                                .message("success")
                                .data(chatService.findAllMessage(userId))
                                .build());
    }

    @Async(value = "asyncThreadPool")
    @GetMapping("/chat/room/{roomId}")
    public ResponseEntity<ResponseDTO> findAllMessageByRoomAndCreateDate(@PathVariable Integer roomId){
        return ResponseEntity.ok()
                .body(ResponseDTO.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("success")
                        .data(chatService.findAllMessageByRoom(roomId))
                        .build());
    }
}