package faang.school.analytics.listener.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewProfileEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewProfileEvent> {

    public ProjectViewEventListener(AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper,
                                    ObjectMapper objectMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectViewProfileEvent.class, event -> {
            analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEventEntity(event));
        });
    }

}
