package faang.school.analytics.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.dto.event.projectViewEvent.ProjectViewEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {

    protected ProjectViewEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService,
                                       AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectViewEvent.class, analyticsEventMapper::toAnalyticsFromProjectView);
    }
}
