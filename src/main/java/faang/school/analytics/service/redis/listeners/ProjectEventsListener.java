package faang.school.analytics.service.redis.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.ProjectEventsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import faang.school.analytics.service.redis.events.ProjectEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectEventsListener implements MessageListener {
    private final AnalyticsService analyticsService;

    private final ProjectEventsMapper projectEventsMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProjectEvent projectEvent = null;
        try {
            projectEvent = objectMapper.readValue(message.getBody(), ProjectEvent.class);
            AnalyticsEvent analyticsEvent = projectEventsMapper.toAnalyticsEvent(projectEvent);

            analyticsService.create(analyticsEvent);

            log.info("Received message: " + "Project with id: " + projectEvent.getProjectId() + " was " + analyticsEvent.getEventType());
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}