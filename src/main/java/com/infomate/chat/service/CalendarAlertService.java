package com.infomate.chat.service;

import com.infomate.chat.dto.CalendarAlertDTO;
import com.infomate.chat.entity.CalendarAlert;
import com.infomate.chat.repository.CalendarAlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class CalendarAlertService {

    private final CalendarAlertRepository calendarAlertRepository;

    private final ModelMapper modelMapper;

    public List<CalendarAlertDTO> findSchedule(LocalDateTime localDateTime) {
//        log.info("[CalendarAlertService](findSchedule) ZoneDateTime: {}",localDateTime.plusMinutes(30).atZone(ZoneId.of("UTC")));
        List<CalendarAlert> calendarAlertList =
                calendarAlertRepository.findAllByEndDateBetween(
                        localDateTime,
                        localDateTime.plusMinutes(2).withSecond(0).withNano(999)
                );
//        List<CalendarAlert> calendarAlertList = calendarAlertRepository.findAll();

        if(calendarAlertList.size() == 0) return null;

        return calendarAlertList.stream()
                .map(calendarAlert -> modelMapper.map(calendarAlert, CalendarAlertDTO.class))
                .collect(Collectors.toList());

    }

    public boolean insertCalendarAlert(CalendarAlertDTO calendarAlertDTO) {

        log.info("[CalendarAlertService](insertCalendarAlert) calendarAlertDTO: {}",calendarAlertDTO);

        CalendarAlert calendarAlert = modelMapper.map(calendarAlertDTO, CalendarAlert.class);

        log.info("[CalendarAlertService](insertCalendarAlert) calendarAlert: {}",calendarAlert);

        calendarAlertRepository.save(calendarAlert);

        return true;
    }
}
