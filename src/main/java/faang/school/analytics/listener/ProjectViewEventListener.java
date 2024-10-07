package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectViewEventListener {

    private final ObjectMapper objectMapper;
    private final AnalyticService analyticService;

    @EventListener
    public void handleMessage(String jsonEvent) {
        log.info("Event processing: {}", jsonEvent);
        ProjectViewEvent event = readEvent(jsonEvent);
        analyticService.saveAnalyticEvent(event);
        log.info("Event processed: {}", event);
    }

    private ProjectViewEvent readEvent(String jsonEvent) {
        try {
            return objectMapper.readValue(jsonEvent, ProjectViewEvent.class);
        } catch (JsonProcessingException exception) {
            log.error("Error parsing json event: {}", jsonEvent, exception);
            throw new RuntimeException(exception);
        }
    }
}
