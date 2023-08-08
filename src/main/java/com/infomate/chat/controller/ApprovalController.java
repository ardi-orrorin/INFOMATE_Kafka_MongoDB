package com.infomate.chat.controller;

import com.infomate.chat.dto.ApprovalDTO;
import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@RestController
@RequiredArgsConstructor
@RequestMapping("/approval")
@Slf4j
public class ApprovalController {

    private final ApprovalService approvalService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    private final KafkaTemplate<String, ApprovalDTO> kafkaTemplate;

    @PostMapping("/insert")
    public void insertApprovalAlert(@RequestBody ApprovalDTO approvalDTO){

        log.info("[ApprovalController](insertApprovalAlert) approvalDTO : {}", approvalDTO);

        CompletableFuture<SendResult<String, ApprovalDTO>> future =
                kafkaTemplate.send("topic02", approvalDTO);
        future.whenComplete(new BiConsumer<SendResult<String, ApprovalDTO>, Throwable>() {
            @Override
            public void accept(SendResult<String, ApprovalDTO> stringMessageDTOSendResult, Throwable throwable) {
                log.info("[ApprovalController](insertApprovalAlert) message : {}", approvalDTO);
                log.info("[ApprovalController](insertApprovalAlert) 메세지 전송 성공");

                approvalService.insertApproval(approvalDTO);
            }
        });
    }


    @Async(value = "asyncThreadPool")
    @KafkaListener(topics = "topic02", groupId = "foo")
    public void publisher(@Payload ApprovalDTO approvalDTO){
        log.info("[ApprovalController](publisher) receiver : {}", approvalDTO);

        simpMessageSendingOperations.convertAndSend("/sub/approval/" + approvalDTO.getReceiver(), approvalDTO);

    }
}
