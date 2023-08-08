package com.infomate.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ApprovalDTO {

    private int receiver;

    private int sender;

    private String subject;

    private String url;

    private LocalDateTime approvalDate;
}
