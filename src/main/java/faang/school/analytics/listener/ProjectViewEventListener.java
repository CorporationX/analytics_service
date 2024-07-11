package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEventDto> {

    public ProjectViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(ProjectViewEventDto.class, objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    protected void saveEvent(ProjectViewEventDto event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toProjectEntity(event);
        analyticsEvent.setEventType(EventType.PROJECT_VIEW);
        analyticsEventService.saveEvent(analyticsEvent);
    }

}