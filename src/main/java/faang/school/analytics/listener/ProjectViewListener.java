package faang.school.analytics.listener;

import faang.school.analytics.dto.ProjectViewEventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectViewListener extends AbstractEventListener<ProjectViewEventDto> {

    @Autowired
    private AnalyticsEventService analyticsService;

    public ProjectViewListener() {
        super(ProjectViewEventDto.class);
    }

    @Override
    public void saveEvent(ProjectViewEventDto event) {
        AnalyticsEvent analyticsEvent = eventMapper.toAnalyticsEvent(event);
        analyticsService.saveEvent(analyticsEvent);
    }
}