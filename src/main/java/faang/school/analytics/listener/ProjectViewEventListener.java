package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProjectViewEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;

    @Async
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("InviteEventListener has received a new message from Redis")
        Map<String, String> data;
        try {
            String jsonMessage = new String((byte[]) message.getBody());
            data = new ObjectMapper().readValue(jsonMessage, Map.class);
        } catch (JsonProcessingException e) {
            log.info("Error parsing JSON message");
            throw new SerializationException("Error parsing JSON message");
        }

        String userId = (String) data.get("userId");
        String projectId = (String) data.get("projectId");
        String timestamp = data.get("timestamp");

        log.info("Received ProjectViewEvent: userId={}, projectId={}, timestamp={}", userId, projectId, timestamp);


        AnalyticsEventDto eventDto = new AnalyticsEventDto();
        eventDto.setReceiverId(Long.parseLong(projectId));
        eventDto.setActorId(Long.parseLong(userId));
        eventDto.setEventType(EventType.PROJECT_VIEW);
        eventDto.setReceivedAt(LocalDateTime.parse(timestamp));
        analyticsEventService.saveEvent(eventDto);
    }
}
