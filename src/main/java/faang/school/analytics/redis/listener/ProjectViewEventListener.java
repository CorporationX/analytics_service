package faang.school.analytics.redis.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.ProjectViewEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> {

    public ProjectViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsService, analyticsEventMapper, ProjectViewEvent.class);
    }

    @Override
    protected AnalyticsEvent mapEvent(ProjectViewEvent event) {
        return analyticsEventMapper.toAnalyticsProjectEvent(event);
    }

}

