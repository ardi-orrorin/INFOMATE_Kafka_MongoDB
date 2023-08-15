package com.infomate.chat.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class ChatDTO {

    private int sender;

    private int chatRoomNo;

    private List<Integer> receiveList;

    private LocalDateTime createDate;

    private Object message;

}
