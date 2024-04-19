package faang.school.analytics.listener.projectview;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> {

    public ProjectViewEventListener(ObjectMapper objectMapper, AnalyticsService analyticsService, AnalyticsEventMapper analyticsEventMapper, Class<ProjectViewEvent> eventType) {
        super(objectMapper, analyticsService, analyticsEventMapper, eventType);
    }

    @Override
    protected faang.school.analytics.model.AnalyticsEvent mapEvent(ProjectViewEvent event) {
        return analyticsEventMapper.toAnalyticsEvent(event);
    }
}
