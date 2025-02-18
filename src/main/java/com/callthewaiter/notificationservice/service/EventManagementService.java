package com.callthewaiter.notificationservice.service;

import com.callthewaiter.notificationservice.dto.PreviousNNotificationsResponseDto;
import com.callthewaiter.notificationservice.dto.WaiterAcceptEventResponseDto;
import com.callthewaiter.notificationservice.exceptions.EventNotFoundException;
import com.callthewaiter.notificationservice.model.Event;
import com.callthewaiter.notificationservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventManagementService {
    private final EventRepository eventRepository;
    private final SimpMessagingTemplate messagingTemplate;
    public WaiterAcceptEventResponseDto acknowledgeEventByWaiter(String eventId){
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        event.setStatus("acknowledged");
        eventRepository.save(event);
        String sessionId = event.getSessionId();
        String destination = "/topic/waiter-acknowledged/"+sessionId;
        messagingTemplate.convertAndSend(destination, event);
        return WaiterAcceptEventResponseDto
                .builder()
                .id(event.getId())
                .status(event.getStatus())
                .build();
    }
    public PreviousNNotificationsResponseDto getPreviousNEvents(String waiterId) {
        List<PreviousNNotificationsResponseDto.NotificationDto> notifications =
                eventRepository.findTop10ByWaiterIdOrderByTimestampDesc(waiterId)
                        .stream()
                        .map(event -> PreviousNNotificationsResponseDto.NotificationDto.builder()
                                .id(event.getId())
                                .status(event.getStatus())
                                .tableNumber(event.getTableNumber())
                                .timestamp(event.getTimestamp())
                                .build())
                        .collect(Collectors.toList());

        return PreviousNNotificationsResponseDto.builder()
                .notifications(notifications)
                .message("Previous notifications fetched successfully")
                .build();
    }
}
