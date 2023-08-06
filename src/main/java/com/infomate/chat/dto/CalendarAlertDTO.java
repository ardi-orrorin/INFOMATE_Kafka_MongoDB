package com.infomate.chat.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

public class CalendarAlertDTO {

    private int memberCode;

    private int scheduleId;

    private String calendarName;

    private String scheduleTitle;

    private LocalDateTime endDate;

    private Boolean important;

}
