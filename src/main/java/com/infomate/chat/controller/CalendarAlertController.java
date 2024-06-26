package com.infomate.chat.controller;


import com.infomate.chat.dto.CalendarAlertDTO;
import com.infomate.chat.service.CalendarAlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/calendar")
@Slf4j
@RequiredArgsConstructor
public class CalendarAlertController {

    private final CalendarAlertService calendarAlertService;

    @PostMapping("/alert")
    public ResponseEntity<?> insertCalendarAlert(@RequestBody CalendarAlertDTO calendarAlertDTO){
        log.info("[CalendarAlertController](insertCalendarAlert) calendarAlertDTO : {}", calendarAlertDTO);

        boolean result = calendarAlertService.insertCalendarAlert(calendarAlertDTO);

        log.info("[CalendarAlertController](insertCalendarAlert) result : {}", result);

        if(!result) return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).build();

        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @PutMapping("/alert")
    public ResponseEntity<?> updateCalendarAlert(@RequestBody CalendarAlertDTO calendarAlertDTO){
        log.info("[CalendarAlertController](updateCalendarAlert) calendarAlertDTO : {}", calendarAlertDTO);

        calendarAlertService.updateCalendarAlert(calendarAlertDTO);

        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

    @DeleteMapping("/alert")
    public ResponseEntity<?> deleteCalednarAlert(@RequestBody Integer scheduleId){
        calendarAlertService.deleteSchedule(Mono.just(scheduleId));
        return ResponseEntity.status(HttpStatus.OK.value()).build();
    }

}
