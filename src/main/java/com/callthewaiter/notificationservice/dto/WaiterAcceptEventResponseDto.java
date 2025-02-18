package com.callthewaiter.notificationservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WaiterAcceptEventResponseDto {
    private String id;
    private String status;
}
