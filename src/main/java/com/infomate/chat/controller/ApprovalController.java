package com.infomate.chat.controller;

import com.infomate.chat.dto.ApprovalDTO;
import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.function.BiConsumer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/approval")
@Slf4j
public class ApprovalController {

    private final KafkaTemplate<String , MessageDTO> kafkaTemplate;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final ApprovalService approvalService;

    @PostMapping("/insert")
    public void insertApprovalAlert(@RequestBody ApprovalDTO approvalDTO){

        Mono<ApprovalDTO> approvalDTOMono = Mono.just(approvalDTO);
        log.info("[ApprovalController](insertApprovalAlert) approvalDTO : {}", approvalDTO);
        Mono<MessageDTO> messageDTO = approvalDTOMono.map(approvalDTO1 ->
                MessageDTO.builder()
                        .sender(approvalDTO1.getSender())
                        .url(approvalDTO1.getUrl())
                        .createDate(approvalDTO1.getApprovalDate())
                        .receiveList(Arrays.asList(approvalDTO1.getReceiver()))
                        .subject(approvalDTO1.getSubject())
                        .build()
        );

        log.info("[ApprovalController](insertApprovalAlert) approvalDTO : {}", approvalDTO);

        messageDTO.subscribe(messageDTO1 -> kafkaTemplate.send("topic02", messageDTO1).whenComplete(
                new BiConsumer<SendResult<String, MessageDTO>, Throwable>() {
                    @Override
                    public void accept(SendResult<String, MessageDTO> stringMessageDTOSendResult, Throwable throwable) {
                        log.info("[ApprovalController](insertApprovalAlert) message : {}", approvalDTO);
                        log.info("[ApprovalController](insertApprovalAlert) 메세지 전송 성공");


                    }
                })
        );

        approvalService.insertApproval(approvalDTOMono);

        log.info("[ApprovalController](insertApprovalAlert) approvalDTO : {}", approvalDTO);

    }

    @Async(value = "asyncThreadPool")
    @KafkaListener(topics = "topic02", groupId = "foo")
    public void publisher(@Payload MessageDTO messageDTO){
        log.info("[ApprovalController](publisher) messageDTO : {}", messageDTO);

        messageDTO.getReceiveList().forEach(receiver ->
                simpMessageSendingOperations.convertAndSend("/sub/approval/" + receiver, messageDTO)
        );


    }



}
