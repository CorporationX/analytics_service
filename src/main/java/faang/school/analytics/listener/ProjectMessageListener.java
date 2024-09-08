package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectMessageListener extends RedisAbstractMessageListener<ProjectViewEvent> implements MessageListener {

    private final AnalyticsEventMapper analyticsEventMapper;

    public ProjectMessageListener(AnalyticsEventMapper mapper,
                                  AnalyticsEventService analyticsEventService,
                                  ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper) {
        super(mapper, analyticsEventService, objectMapper, ProjectViewEvent.class);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    AnalyticsEvent map(ProjectViewEvent projectViewEvent) {
        return analyticsEventMapper.toAnalyticsEvent(projectViewEvent);
    }
}
