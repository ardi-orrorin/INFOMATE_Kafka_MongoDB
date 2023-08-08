package com.infomate.chat.service;

import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Chat;
import com.infomate.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRepository chatRepository;

    private final ModelMapper modelMapper;

    @Transactional
    public void insertMessage(MessageDTO message) {
        log.info("[ChatService](insertMessage) message : {}", message);

        Chat messageEntity = modelMapper.map(message, Chat.class);
        log.info("[ChatService](insertMessage) message : {}", messageEntity);

        chatRepository.save(messageEntity);
    }

    public Flux<Chat> findMessageByDay(Mono<Integer> roomNo, Mono<Integer> memberCode, Mono<LocalDate> day) {

        Flux<Chat> messageList =
                chatRepository.findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(
                        memberCode,
                        roomNo,
                        Mono.just(Sort.by(Sort.Direction.ASC, "createDate")),
                        day.map(days -> days.atTime(LocalTime.MAX)),
                        day.map(days -> days.atTime(LocalTime.MIN))
                );

        log.info("[ChatService](findMessageByDay) messageList : {}", messageList);

        return messageList;
    }

//    @Async(value = "asyncThreadPool")
//    public CompletableFuture<List<MessageDTO>> findAllMessageByRoom(Integer roomId) {
//        System.out.println("LocalTime.MIN = " + LocalTime.MIN);
//        LocalDateTime beforDate = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime afterDate = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//        Flux<Message> messageList =
//                chatRepository.findAllByChatRoomNoAndCreateDateBetween(roomId, beforDate, afterDate);
//        return CompletableFuture.completedFuture(messageList.stream().map(message -> modelMapper.map(message, MessageDTO.class)).collect(Collectors.toList()));
//    }

}
