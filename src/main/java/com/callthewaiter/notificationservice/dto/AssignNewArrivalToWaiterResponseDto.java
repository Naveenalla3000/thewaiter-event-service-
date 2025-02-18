package com.callthewaiter.notificationservice.dto;

import lombok.Data;

@Data
@lombok.Builder
public class AssignNewArrivalToWaiterResponseDto {
    private String waiterId;
    private String message;
}