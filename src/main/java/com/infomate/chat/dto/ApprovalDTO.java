package com.infomate.chat.dto;

import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApprovalDTO {

    private ObjectId id;

    private int receiver;

    private String sender;

    private String subject;

    private String url;

    private LocalDateTime approvalDate;
}
