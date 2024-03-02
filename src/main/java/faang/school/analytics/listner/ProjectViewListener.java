package faang.school.analytics.listner;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.base.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectViewListener extends AbstractEventListener<ProjectViewEvent> {
    public ProjectViewListener(ObjectMapper objectMapper,
                               AnalyticsEventService analyticsEventService,
                               AnalyticsEventMapper<ProjectViewEvent> analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        saveAnalyticsEvent(message, ProjectViewEvent.class);
    }
}