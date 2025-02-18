package com.callthewaiter.notificationservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class PreviousNNotificationsResponseDto {
    private List<NotificationDto> notifications;
    private String message;

    @Data
    @Builder
    public static class NotificationDto {
        private String id;
        private String status;
        private Integer tableNumber;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        private LocalDateTime timestamp;
    }
}
