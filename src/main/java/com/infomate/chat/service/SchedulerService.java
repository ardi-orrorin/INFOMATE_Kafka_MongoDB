package com.infomate.chat.service;

import com.infomate.chat.dto.CalendarAlertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final CalendarAlertService calendarAlertService;

    // cron = "초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7)
    @Scheduled(cron = "59 * * * * *")
    @Async(value = "asyncThreadPool")
    public void calendarAlert(){
        LocalDateTime currentTime = LocalDateTime.now().withNano(0);
        log.info("[CalendarAlertController](publisher) currentTime : {}", currentTime);
        List<CalendarAlertDTO> calendarAlertList =
                calendarAlertService.findSchedule(currentTime);

        log.info("[CalendarAlertController](publisher) calendarAlertList : {}", calendarAlertList);


//        log.info("[CalendarAlertController](publisher) memberCode : {}",memberCode);
    }
}
