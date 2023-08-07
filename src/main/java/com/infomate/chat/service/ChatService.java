package com.infomate.chat.service;

import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Message;
import com.infomate.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public void insertMessage(MessageDTO message) {
        log.info("[ChatService](insertMessage) message : {}", message);
        Message messageEntity = modelMapper.map(message, Message.class);
        log.info("[ChatService](insertMessage) message : {}", messageEntity);
        chatRepository.save(messageEntity);
        log.info("[ChatService](insertMessage) message : {}", messageEntity);
    }

//    }
    public List<Message> findAllMessage(Integer userId) {

        List<Message> messageList =

                chatRepository.findAllByReceiveListContaining(Arrays.asList(userId), Sort.by(Sort.Direction.DESC, "createDate"));

        return messageList;
    }

    @Async(value = "asyncThreadPool")
    public CompletableFuture<List<MessageDTO>> findAllMessageByRoom(Integer roomId) {
        System.out.println("LocalTime.MIN = " + LocalTime.MIN);
        LocalDateTime beforDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime afterDate = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<Message> messageList =
                chatRepository.findAllByChatRoomNoAndCreateDateBetween(roomId, beforDate, afterDate);
        return CompletableFuture.completedFuture(messageList.stream().map(message -> modelMapper.map(message, MessageDTO.class)).collect(Collectors.toList()));
    }

    public Message findMessage(Integer userId) {
        return chatRepository.findFirstByReceiveListContaining(userId);
    }
}
