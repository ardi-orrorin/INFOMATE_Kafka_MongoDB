package com.infomate.chat.service;


import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Message;
import com.infomate.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public void insertMessage(MessageDTO message) {
        log.info("[ChatService](insertMessage) message : {}", message);

        Message messageEntity = modelMapper.map(message, Message.class);

        log.info("[ChatService](insertMessage) messageEntity : {}", messageEntity);

        Mono<Message> messageMono = messageRepository.insert(messageEntity);
        messageMono.subscribe(e->
                log.info("[ChatService](insertMessage) chatMono : {}", e));
    }

    public Flux<MessageDTO> findMessageByDay(int roomNo, int memberCode, LocalDate date) {

        return messageRepository.findAllByChatRoomNoAndCreateDateBetween(
                roomNo,
                LocalDateTime.of(date, LocalTime.MIN),
                LocalDateTime.of(date, LocalTime.MAX),
                Sort.by(Sort.Direction.ASC, "createDate")
        ).map(message -> modelMapper.map(message, MessageDTO.class)).log();
    }
}
