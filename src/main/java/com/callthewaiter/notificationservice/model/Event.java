package com.callthewaiter.notificationservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "events")
public class Event {

    @MongoId
    private String id;
    private String sessionId;
    private String restaurantId;
    private Integer tableNumber;
    private String status;
    private String waiterId;
    private LocalDateTime timestamp;
}
