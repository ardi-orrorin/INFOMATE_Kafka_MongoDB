package com.infomate.chat.service;

import com.infomate.chat.dto.CalendarAlertDTO;
import com.infomate.chat.entity.CalendarAlert;
import com.infomate.chat.repository.CalendarAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarAlertService {

    private final CalendarAlertRepository calendarAlertRepository;

    private final ModelMapper modelMapper;

    public Flux<CalendarAlertDTO> findSchedule(LocalDateTime localDateTime) {
        log.info("[CalendarAlertService](findSchedule) localDateTime: {}",localDateTime);

        Flux<CalendarAlert> calendarAlertList =
                calendarAlertRepository.findAllByAlertDateBetween(
                        localDateTime,
                        localDateTime.plusMinutes(2).withSecond(0).withNano(999)
                );

        log.info("[CalendarAlertService](findSchedule) calendarAlertList: {}", calendarAlertList);

        return calendarAlertList.map(calendarAlert ->
                modelMapper.map(calendarAlert, CalendarAlertDTO.class));


    }

    @Transactional
    public boolean insertCalendarAlert(CalendarAlertDTO calendarAlertDTO) {

        log.info("[CalendarAlertService](insertCalendarAlert) calendarAlertDTO: {}",calendarAlertDTO);


        CalendarAlert calendarAlert = modelMapper.map(calendarAlertDTO, CalendarAlert.class);
        log.info("[CalendarAlertService](insertCalendarAlert) calendarAlert: {}", calendarAlert);

        calendarAlertRepository.save(calendarAlert).subscribe();

        return true;
    }

    @Transactional
    public void deleteScheduleList(Flux<CalendarAlertDTO> calendarAlertList) {

     Flux<CalendarAlert> calendarAlertListEntity =
             calendarAlertList.map(calendarAlertDTO ->
                             modelMapper.map(calendarAlertDTO, CalendarAlert.class));

        calendarAlertRepository.deleteAll(calendarAlertListEntity).subscribe();
    }

    @Transactional
    public void deleteSchedule(Mono<Integer> scheduleId){
        scheduleId.subscribe(id ->
            calendarAlertRepository.deleteByScheduleId(id)
        );
    }


    @Transactional
    public void updateCalendarAlert(CalendarAlertDTO calendarAlertDTO) {
        Mono<CalendarAlert> calendarAlertMono = calendarAlertRepository.findByScheduleId(calendarAlertDTO.getScheduleId());
//        calendarAlertMono.map(calendarAlert -> {
//            calendarAlert.update(calendarAlertDTO);
//            return calendarAlert;
//        }).subscribe();
        calendarAlertMono.subscribe(calendarAlert -> {
            calendarAlert.update(calendarAlertDTO);
            calendarAlertRepository.save(calendarAlert).subscribe();
        });



    }
}

