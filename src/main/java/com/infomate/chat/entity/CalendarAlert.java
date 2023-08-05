package com.infomate.chat.entity;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Document(collection = "CalendarAlert")
public class CalendarAlert {

    @MongoId
    private ObjectId id;

    @Field(name = "memberCode")
    private int memberCode;

    @Field(name = "scheduleId")
    private int scheduleId;

    @Field(name = "calendarName")
    private String calendarName;

    @Field(name = "scheduleTitle")
    private String scheduleTitle;

    @Field(name = "endDate")
    private LocalDateTime endDate;

    @Field(name = "important")
    private Boolean important;
}
