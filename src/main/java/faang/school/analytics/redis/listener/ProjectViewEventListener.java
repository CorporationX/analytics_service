package faang.school.analytics.redis.listener;

import faang.school.analytics.AnalyticsEventMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectViewEventListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public ProjectViewEventListener(AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @EventListener
    public void handleProjectViewEvent(ProjectViewEvent projectViewEvent) {
        // Преобразование ProjectViewEvent в AnalyticsEvent с использованием analyticsEventMapper
        AnalyticsEvent analyticsEvent = analyticsEventMapper.mapToAnalyticsEvent(projectViewEvent);

        analyticsEventService.saveAnalyticsEvent(analyticsEvent);
    }
}
