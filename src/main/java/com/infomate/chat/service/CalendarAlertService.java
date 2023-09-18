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
import java.util.Optional;


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
            calendarAlertRepository.deleteByScheduleId(id).subscribe()
        );
    }


    @Transactional
    public void updateCalendarAlert(CalendarAlertDTO calendarAlertDTO) {
        log.info("[CalendarAlertService](updateCalendarAlert) calendarAlertDTO : {} ", calendarAlertDTO);
        Mono<CalendarAlert> calendarAlertMono = calendarAlertRepository.findByScheduleId(calendarAlertDTO.getScheduleId());

        log.info("[CalendarAlertService](updateCalendarAlert) calendarAlertMono : {} ", calendarAlertMono);


        calendarAlertMono.map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .subscribe( calendarAlert -> {
                    log.info("[CalendarAlertService](updateCalendarAlert) calendarAlertMono.subscribe calendarAlert : {} ", calendarAlert);
                    if(calendarAlert.isEmpty() || !calendarAlert.get().getAlertDate().isAfter(LocalDateTime.now().plusMinutes(30))){
                        insertCalendarAlert(calendarAlertDTO);
                    }else{
                        calendarAlert.get().update(calendarAlertDTO);
                        calendarAlertRepository.save(calendarAlert.get()).subscribe();
                    }
                });



    }
}

