package com.infomate.chat.repository;

import com.infomate.chat.entity.Message;
import com.mongodb.client.model.Sorts;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends MongoRepository<Message, ObjectId> {


    List<Message> findAllByReceiveListContaining(List<Integer> receiveList, Sort sort);

    List<Message> findAllByChatRoomNoAndCreateDateBetween(Integer roomId, LocalDateTime beforeDate, LocalDateTime afterDate);
}
