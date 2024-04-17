package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProjectViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class ProjectViewEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Async
    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("InviteEventListener has received a new message from Redis");
        try {
            String jsonMessage = new String((byte[]) message.getBody());
            ProjectViewEventDto projectViewEventDto = new ObjectMapper().readValue(jsonMessage, ProjectViewEventDto.class);
            AnalyticsEventDto eventDto = analyticsEventMapper.toAnalyticsEventDto(projectViewEventDto);
            eventDto.setEventType(EventType.PROJECT_VIEW);
            analyticsEventService.saveEvent(eventDto);
        } catch (JsonProcessingException e) {
            log.info("Error parsing JSON message");
            throw new SerializationException("Error parsing JSON message");
        }
    }
}
