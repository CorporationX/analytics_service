package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class ProjectViewEventListener extends RedisMessageListener<ProjectViewEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public ProjectViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    public void saveEvent(ProjectViewEvent projectViewEvent) {
        analyticsEventService.saveEvent(analyticsEventMapper.projectViewEventToAnalyticsEvent(projectViewEvent));
    }

    @Override
    protected Class<ProjectViewEvent> getEventType() {
        return ProjectViewEvent.class;
    }
}
