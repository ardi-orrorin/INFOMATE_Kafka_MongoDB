package com.infomate.chat.controller;


import com.infomate.chat.dto.CalendarAlertDTO;
import com.infomate.chat.service.CalendarAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/calendar")
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class CalendarAlertController {

    private final CalendarAlertService calendarAlertService;

//    @Async(value = "asyncThreadPool")
    @PostMapping("/alert")
    public CompletableFuture<ResponseEntity<?>> insertCalendarAlert(@RequestBody CalendarAlertDTO calendarAlertDTO){
        log.info("[CalendarAlertController](insertCalendarAlert) calendarAlertDTO : {}", calendarAlertDTO);

        boolean result = calendarAlertService.insertCalendarAlert(calendarAlertDTO);
//        calendarAlertService.insertCalendarAlert(calendarAlertDTO);
        log.info("[CalendarAlertController](insertCalendarAlert) result : {}", result);

        if(!result) return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build());

        return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.OK.value()).build());
    }

}
