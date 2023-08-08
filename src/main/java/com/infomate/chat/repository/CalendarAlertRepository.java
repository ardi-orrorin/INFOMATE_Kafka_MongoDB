package com.infomate.chat.repository;

import com.infomate.chat.entity.CalendarAlert;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CalendarAlertRepository extends MongoRepository<CalendarAlert, ObjectId> {

    List<CalendarAlert> findAllByEndDateBetween(LocalDateTime localDateTime1, LocalDateTime localDateTime2);
}
