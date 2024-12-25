package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.mapper.project_view.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.event.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProjectViewEventListener extends AbstractListenerForAddEvent<ProjectViewEvent> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public ProjectViewEventListener(
            ObjectMapper objectMapper,
            AnalyticsEventMapper analyticsEventMapper,
            AnalyticsEventService analyticsEventService
    ) {
        super(objectMapper);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public Class getEventClass() {
        return ProjectViewEvent.class;
    }

    @Override
    public void handleEvent(ProjectViewEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        analyticsEventService.addNewEvent(analyticsEvent);
    }
}
