package com.infomate.chat.entity;

import com.infomate.chat.dto.CalendarAlertDTO;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;


@Setter
@Getter
@ToString
@Document(collection = "CalendarAlert")
public class CalendarAlert {

    @MongoId
    private ObjectId id;

    @Field(name = "memberCode")
    private Integer memberCode;

    @Field(name = "scheduleId")
    private Integer scheduleId;

    @Field(name = "scheduleTitle")
    private String scheduleTitle;

    @Field(name = "startDate")
    private LocalDateTime startDate;

    @Field(name = "endDate")
    private LocalDateTime endDate;

    @Field(name = "alertDate")
    private LocalDateTime alertDate;

    @Field(name = "important")
    private Boolean important;

    @Field(name = "allDay")
    private Boolean allDay;

    public void update(CalendarAlertDTO calendarAlertDTO) {
        if (calendarAlertDTO.getAlertDate() != null) this.memberCode = calendarAlertDTO.getMemberCode();
        if (calendarAlertDTO.getScheduleId() != null) this.scheduleId = calendarAlertDTO.getScheduleId();
        if (calendarAlertDTO.getScheduleTitle() != null) this.scheduleTitle = calendarAlertDTO.getScheduleTitle();
        if (calendarAlertDTO.getStartDate() != null) this.startDate = calendarAlertDTO.getStartDate();
        if (calendarAlertDTO.getEndDate() != null) this.endDate = calendarAlertDTO.getEndDate();
        if (calendarAlertDTO.getAlertDate() != null) this.alertDate = calendarAlertDTO.getAlertDate();
        if (calendarAlertDTO.getImportant() != null) this.important = calendarAlertDTO.getImportant();
        if (calendarAlertDTO.getAllDay() != null) this.allDay = calendarAlertDTO.getAllDay();
    }
}
