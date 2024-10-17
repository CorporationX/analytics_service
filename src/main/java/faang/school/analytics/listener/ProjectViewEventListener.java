package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.ProjectViewEvent;
import faang.school.analytics.mapper.analytics.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> {

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
        log.info("Got message, trying to handle it");
        handleEvent(message, ProjectViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapEvent(event);
            saveEvent(analyticsEvent);
        });
    }
}
