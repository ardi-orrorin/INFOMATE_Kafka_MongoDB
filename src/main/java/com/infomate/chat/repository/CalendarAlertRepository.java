package com.infomate.chat.repository;

import com.infomate.chat.entity.CalendarAlert;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CalendarAlertRepository extends ReactiveMongoRepository<CalendarAlert, ObjectId> {

    Flux<CalendarAlert> findAllByAlertDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2);

    Mono<CalendarAlert> findByScheduleId(Integer scheduleId);

    void deleteByScheduleId(Integer scheduleId);
}
