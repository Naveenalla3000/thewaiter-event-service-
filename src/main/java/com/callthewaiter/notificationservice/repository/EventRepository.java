package com.callthewaiter.notificationservice.repository;

import com.callthewaiter.notificationservice.dto.PreviousNNotificationsResponseDto;
import com.callthewaiter.notificationservice.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, Serializable> {
    List<PreviousNNotificationsResponseDto.NotificationDto>
    findTop10ByWaiterIdOrderByTimestampDesc(String waiterId);
}

