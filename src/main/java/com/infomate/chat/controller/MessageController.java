package com.infomate.chat.controller;


import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Message;
import com.infomate.chat.service.MessageService;
import com.infomate.chat.common.ResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.function.BiConsumer;

@RestController
@Slf4j
@EnableMongoAuditing
@EnableAsync
public class MessageController {

    private final MessageService chatService;

    private final KafkaTemplate<String , MessageDTO> kafkaTemplate;

    private final SimpMessageSendingOperations simpMessageSendingOperations;


    @Value("${server.first.api-token}")
    private String FIRST_SERVER_API;

    @Value("${server.first.host}")
    private String FIRST_SERVER;

    public MessageController(MessageService chatService, KafkaTemplate<String, MessageDTO> kafkaTemplate, SimpMessageSendingOperations simpMessageSendingOperations) {
        this.chatService = chatService;
        this.kafkaTemplate = kafkaTemplate;
        this.simpMessageSendingOperations = simpMessageSendingOperations;
    }


    @MessageMapping("/chat/{receiver}")
    public void subScriber(@DestinationVariable Integer receiver, MessageDTO message, SimpMessageHeaderAccessor simpMessageHeaderAccessor) {
        log.info("[ChatController](subScriber) simpMessageHeaderAccessor : {}", simpMessageHeaderAccessor);
        log.info("[ChatController](subScriber) receiver : {}", receiver);
        log.info("[ChatController](subScriber) message : {}", message);

        message.setCreateDate(LocalDateTime.now());
        message.setChatRoomNo(receiver);

        Mono.just(message).subscribe(messageDTO ->
                kafkaTemplate.send("topic01", messageDTO).whenComplete(
                        new BiConsumer<SendResult<String, MessageDTO>, Throwable>() {
                            @Override
                            public void accept(SendResult<String, MessageDTO> stringMessageDTOSendResult, Throwable throwable) {
                                log.info("[ChatController](subScriber) message : {}", message);
                                log.info("[ChatController](subScriber) 메세지 전송 성공");
                                chatService.insertMessage(messageDTO); // 작업 안함
                                }
                        }
                )
        );
    }


    @KafkaListener(topics = "topic01", groupId = "1")
    public void publisher(@Payload MessageDTO message, SimpMessageHeaderAccessor accessor){

        log.info("[ChatController](publisher) receiver : {}", message);
        log.info("[ChatController](publisher) accessor : {}", accessor);

//        message.getReceiveList().forEach(member -> {
//                    log.info("[ChatController](publisher)  member : {}", member);
//                    if (member != message.getSender()) {
//                        simpMessageSendingOperations.convertAndSend("/sub/chat/" + member, message);
//                    }
//                }
//        );

        simpMessageSendingOperations.convertAndSend("/sub/chat/" + message.getChatRoomNo(), message);

    }
    
    @GetMapping("/chat/{roomNo}/{memberCode}")
    public ResponseEntity<Flux<MessageDTO>> findAllMessage(@PathVariable int roomNo,
                                        @PathVariable int memberCode){

        log.info("[ChatController](findAllMessage) : roomNo : {} ", roomNo);
        log.info("[ChatController](findAllMessage) : memberCode : {} ", memberCode);
//        log.info("[ChatController](findAllMessage) : date : {} ", date);


        return ResponseEntity.ok()
                .body(chatService.findMessageByDay(roomNo, memberCode, LocalDate.now()));
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
