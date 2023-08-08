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
    }

    public List<Message> findMessageByDay(Integer roomNo, Integer memberCode, LocalDate day) {

        List<Message> messageList =
                chatRepository.findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(
                        Arrays.asList(memberCode),
                        roomNo,
                        Sort.by(Sort.Direction.ASC, "createDate"),
                        day.atTime(LocalTime.MAX),
                        day.atTime(LocalTime.MIN)
                );

        log.info("[ChatService](findMessageByDay) messageList : {}", messageList);

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

}
