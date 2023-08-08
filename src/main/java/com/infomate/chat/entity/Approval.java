package com.infomate.chat.entity;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "ApprovalDocument")
public class Approval {

    @MongoId
    private ObjectId id;

    @Field(value = "receiver")
    private int receiver;

    @Field(value = "sender")
    private String sender;

    @Field(value = "subject")
    private String subject;

    @Field(value = "url")
    private String url;

    @Field(value = "approvalDate")
    private LocalDateTime approvalDate;

}
