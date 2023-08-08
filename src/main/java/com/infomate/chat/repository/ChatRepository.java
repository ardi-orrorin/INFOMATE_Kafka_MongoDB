package com.infomate.chat.repository;

import com.infomate.chat.entity.Message;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ChatRepository extends MongoRepository<Message, ObjectId> {


    List<Message> findAllByReceiveListContainingAndChatRoomNoAndCreateDateBetween(List<Integer> receiveList, int RoomNo, Sort sort, LocalDateTime startDate, LocalDateTime endDate);

    List<Message> findAllByChatRoomNoAndCreateDateBetween(Integer roomId, LocalDateTime beforeDate, LocalDateTime afterDate);
}
