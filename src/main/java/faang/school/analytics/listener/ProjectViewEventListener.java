package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.ProjectViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {

    public ProjectViewEventListener(AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectViewEvent projectViewEvent = handleEvent(message, ProjectViewEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(projectViewEvent);
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
