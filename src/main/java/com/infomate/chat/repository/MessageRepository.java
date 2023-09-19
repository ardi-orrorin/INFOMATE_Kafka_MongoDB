package com.infomate.chat.repository;

import com.infomate.chat.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface MessageRepository extends ReactiveMongoRepository<Message, ObjectId> {


//    Flux<Chat> findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(
//            Mono<Integer> receiver, Mono<Integer> RoomNo,
//            Mono<Sort> sort, Mono<LocalDateTime> startDate, Mono<LocalDateTime> endDate);
//    Flux<Message> findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(
//            Integer receiver, Integer RoomNo,
//            Sort sort, LocalDateTime startDate, LocalDateTime endDate);
//    Flux<Message> findAllBySenderAndChatRoomNo(
//            Integer receiver, Integer RoomNo,
//            Sort sort);

    Flux<Message> findAllByChatRoomNoAndCreateDateBetween(int roomNo, LocalDateTime beforeDate, LocalDateTime afterDate, Sort sort);




}
