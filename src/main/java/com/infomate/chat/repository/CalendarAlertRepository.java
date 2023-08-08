package com.infomate.chat.repository;

import com.infomate.chat.entity.CalendarAlert;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface CalendarAlertRepository extends ReactiveMongoRepository<CalendarAlert, ObjectId> {

    Flux<CalendarAlert> findAllByEndDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2);
}
