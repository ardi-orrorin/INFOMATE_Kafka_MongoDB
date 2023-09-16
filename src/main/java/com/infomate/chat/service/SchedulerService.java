package com.infomate.chat.service;

import com.infomate.chat.dto.CalendarAlertDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    private final CalendarAlertService calendarAlertService;

    private final SimpMessageSendingOperations simpMessageSendingOperations;


    //cron = "초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7)
    @Scheduled(cron = "59 * * * * *")
    public void calendarAlert(){

        LocalDateTime currentTime = LocalDateTime.now().withNano(0);
        log.info("[SchedulerService](calendarAlert) currentTime : {}", currentTime);

        Flux<CalendarAlertDTO> calendarAlertList =
                calendarAlertService.findSchedule(currentTime);

        log.info("[SchedulerService](calendarAlert) calendarAlertList : {}", calendarAlertList);

        if(calendarAlertList == null) return ;

        calendarAlertList.subscribe(calendarAlertDTO -> {
            log.info("[SchedulerService](calendarAlert) calendarAlertDTO : {}", calendarAlertDTO);
            simpMessageSendingOperations.convertAndSend("/sub/calendar/alert/"+calendarAlertDTO.getMemberCode() , calendarAlertDTO);
        });

        calendarAlertService.deleteScheduleList(calendarAlertList);

    }
}
