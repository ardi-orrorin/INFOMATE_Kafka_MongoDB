package com.infomate.chat.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.messaging.handler.annotation.Header;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "message")
public class Message {

    @MongoId
    private ObjectId id;

    private int sender;

    private int chatRoomNo;

    private Object message;

    private boolean isRead;

    private LocalDateTime createDate;

}
