package faang.school.analytics.messaging.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.project.ProjectViewEvent;
import faang.school.analytics.mapper.project.ProjectViewEventMapper;
import faang.school.analytics.messaging.listener.AbstractEventListener;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {

    private final ProjectViewEventMapper projectViewEventMapper;

    public ProjectViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService,
                                    ProjectViewEventMapper projectViewEventMapper) {
        super(objectMapper, analyticsEventService);
        this.projectViewEventMapper = projectViewEventMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectViewEvent.class, event -> {
            var analyticsEntity = projectViewEventMapper.toAnalyticsEntity(event);
            analyticsEntity.setEventType(EventType.PROJECT_VIEW);
            persistAnalyticsData(analyticsEntity);
        });
    }
}
