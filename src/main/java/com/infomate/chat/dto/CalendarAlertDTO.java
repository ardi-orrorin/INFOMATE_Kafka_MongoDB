package com.infomate.chat.dto;


import lombok.*;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class CalendarAlertDTO {

    private ObjectId id;

    private Integer memberCode;

    private Integer scheduleId;

    private String scheduleTitle;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private LocalDateTime alertDate;

    private Boolean important;

    private Boolean allDay;


}
