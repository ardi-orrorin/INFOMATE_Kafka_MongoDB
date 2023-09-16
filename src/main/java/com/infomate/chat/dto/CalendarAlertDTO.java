package com.infomate.chat.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
public class CalendarAlertDTO {

    private Integer memberCode;

    private Integer scheduleId;

    private String calendarName;

    private String scheduleTitle;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime alertDate;

    private Boolean important;

    private Boolean allDay;


}
