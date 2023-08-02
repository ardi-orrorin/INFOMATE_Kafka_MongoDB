package com.infomate.chat.repository;

import com.infomate.chat.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends MongoRepository<Message, ObjectId> {


    List<Message> findAllByReceiveListContaining(List<Integer> receiveList);

    List<Message> findAllByChatRoomNoAndCreateDateBetween(Integer roomId, LocalDateTime beforeDate,LocalDateTime afterDate);
}
