package com.infomate.chat.repository;

import com.infomate.chat.entity.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface ChatRepository extends ReactiveMongoRepository<Chat, ObjectId> {


//    Flux<Chat> findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(
//            Mono<Integer> receiver, Mono<Integer> RoomNo,
//            Mono<Sort> sort, Mono<LocalDateTime> startDate, Mono<LocalDateTime> endDate);
    Flux<Chat> findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(
            Integer receiver, Integer RoomNo,
            Sort sort, LocalDateTime startDate, LocalDateTime endDate);
    Flux<Chat> findAllBySenderAndChatRoomNo(
            Integer receiver, Integer RoomNo,
            Sort sort);

    Flux<Chat> findAllByChatRoomNoAndCreateDateBetween(Integer roomId, LocalDateTime beforeDate, LocalDateTime afterDate);
}
