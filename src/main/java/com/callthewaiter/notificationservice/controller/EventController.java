package com.callthewaiter.notificationservice.controller;

import com.callthewaiter.notificationservice.dto.PreviousNNotificationsResponseDto;
import com.callthewaiter.notificationservice.dto.WaiterAcceptEventResponseDto;
import com.callthewaiter.notificationservice.service.EventManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventManagementService eventManagementService;
    @GetMapping("/waiter-accept")
    public ResponseEntity<WaiterAcceptEventResponseDto> handleWaiterAcceptEvent(@RequestParam("id") String id) {
        return ResponseEntity.ok(eventManagementService.acknowledgeEventByWaiter(id));
    }
    @GetMapping("/get-previous-events")
    public ResponseEntity<PreviousNNotificationsResponseDto> getPreviousNEvents(@RequestParam("waiterId") String waiterId) {
        return ResponseEntity.ok(eventManagementService.getPreviousNEvents(waiterId));
    }
}
