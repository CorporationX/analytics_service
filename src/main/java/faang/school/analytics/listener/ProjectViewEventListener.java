package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.ProjectViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component("projectViewEventListener")
@Slf4j
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEventDto> {

    public ProjectViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    protected EventType getEventType() {
        return EventType.PROJECT_VIEW;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectViewEventDto event = handleEvent(message, ProjectViewEventDto.class);
        log.debug("Received project view event: {}", event);
        sendAnalytics(event);
    }
}
