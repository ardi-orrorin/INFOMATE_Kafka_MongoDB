package com.infomate.chat.service;

import com.infomate.chat.dto.MessageDTO;
import com.infomate.chat.entity.Message;
import com.infomate.chat.repository.ReactiveChatRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactiveChatService {

    private final ReactiveChatRepository reactiveChatRepository;

    private final ModelMapper modelMapper;

    public Flux<Message> findAll(Integer memberCode){

        Flux<Message> messageList =
                reactiveChatRepository.findAllByReceiveListContaining(Mono.just(memberCode), Sort.by(Sort.Direction.DESC, "createDate"));

//        return messageList.map(messages -> messages.stream()
//                .map(message -> modelMapper.map(message, MessageDTO.class))
//                .collect(Collectors.toList()));

        return messageList.parallel(10).sequential();
    }


}
