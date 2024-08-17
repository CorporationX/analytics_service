package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ProjectMessageConsumer extends RedisAbstractMessageListener<ProjectViewEvent> {

    public ProjectMessageConsumer(ObjectMapper objectMapper,
                                  AnalyticsEventMapper analyticsEventMapper,
                                  AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            ProjectViewEvent projectViewEvent = objectMapper.readValue(message.getBody(), ProjectViewEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.projectViewEventToAnalyticsEvent(projectViewEvent);
            analyticsEventService.saveEventEntity(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
