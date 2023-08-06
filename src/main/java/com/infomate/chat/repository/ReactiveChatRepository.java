package com.infomate.chat.repository;


import com.infomate.chat.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ReactiveChatRepository extends ReactiveMongoRepository<Message, ObjectId> {

    Flux<Message> findAllByReceiveListContaining(Mono<Integer> receiveList, Sort sort);
}
