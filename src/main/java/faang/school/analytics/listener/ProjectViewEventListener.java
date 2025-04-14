package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProjectViewEventListener implements MessageListener {
    private final ObjectMapper mapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectViewEvent projectViewEvent = mapper.readValue(message.getBody(), ProjectViewEvent.class);
            log.info("Project view event received: {}", projectViewEvent);
            AnalyticsEventDto analyticsEventDto = AnalyticsEventDto.builder()
                    .actorId(projectViewEvent.getUserId())
                    .receiverId(projectViewEvent.getProjectId())
                    .receivedAt(projectViewEvent.getTimestamp())
                    .eventType(EventType.PROJECT_VIEW)
                    .build();
            analyticsEventService.saveEvent(analyticsEventDto);
        } catch (IOException e) {
            log.error("Can't read message from redis {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
