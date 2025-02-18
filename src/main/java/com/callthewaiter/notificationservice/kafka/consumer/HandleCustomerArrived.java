package com.callthewaiter.notificationservice.kafka.consumer;

import com.callthewaiter.notificationservice.dto.AssignNewArrivalToWaiterResponseDto;
import com.callthewaiter.notificationservice.model.Event;
import com.callthewaiter.notificationservice.repository.EventRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@Service
@AllArgsConstructor
public class HandleCustomerArrived {

    private final EventRepository eventRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @KafkaListener(topics = "waiter-topic", groupId = "waiter-group")
    public void subscribe(String payload) {
        try {
            System.out.println("Received payload: " + payload);

            JsonNode jsonNode = objectMapper.readTree(payload);

            ResponseEntity<AssignNewArrivalToWaiterResponseDto> response =
                    restTemplate.getForEntity("http://localhost:8083/api/public/waiters/assign?restaurantId=" + jsonNode.get("restaurantId").asText(), AssignNewArrivalToWaiterResponseDto.class);

            Event event = Event.builder()
                    .sessionId(jsonNode.has("sessionId") ? jsonNode.get("sessionId").asText() : null)
                    .restaurantId(jsonNode.has("restaurantId") ? jsonNode.get("restaurantId").asText() : null)
                    .tableNumber(jsonNode.has("tableNumber") ? jsonNode.get("tableNumber").asInt() : null)
                    .status(jsonNode.has("status") ? jsonNode.get("status").asText() : "unknown")
                    .waiterId(Objects.requireNonNull(response.getBody()).getWaiterId())
                    .timestamp(jsonNode.has("timestamp") ? LocalDateTime.parse(jsonNode.get("timestamp").asText(), formatter) : LocalDateTime.now())
                    .build();
            eventRepository.save(event);
            System.out.println("Saved event: " + event);
            String endPoint = "/topic/waiter-updates/"+Objects.requireNonNull(response.getBody()).getWaiterId();
            System.out.println("endPoint is "+endPoint);
            messagingTemplate.convertAndSend(endPoint, event);
            System.out.println("Sent event to waiter: " + event);
        } catch (Exception e) {
            System.err.println("Error In Handling Event: " + e.getMessage());
        }
    }
}
