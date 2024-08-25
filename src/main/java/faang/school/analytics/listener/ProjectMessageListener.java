package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProjectMessageListener extends RedisAbstractMessageListener<ProjectViewEvent> implements MessageListener {

    private final Class<ProjectViewEvent> eventClass;

    public ProjectMessageListener(ObjectMapper objectMapper,
                                  AnalyticsEventMapper analyticsEventMapper,
                                  AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
        this.eventClass = ProjectViewEvent.class;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectViewEvent projectViewEvent = handleEvent(eventClass, message);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(projectViewEvent);
        analyticsEventService.saveEventEntity(analyticsEvent);
    }
}
