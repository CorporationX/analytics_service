package faang.school.analytics.listener;

import faang.school.analytics.dto.EventDto;
import faang.school.analytics.dto.ProjectViewDto;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.mapper.ProjectViewEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import static faang.school.analytics.model.EventType.PROJECT_VIEW;

@Component
@RequiredArgsConstructor
public class ProjectViewEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final ProjectViewEventMapper eventMapper;
    private final JsonObjectMapper jsonObjectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ProjectViewDto projectViewDto = jsonObjectMapper.readValue(message.getBody(), ProjectViewDto.class);
        EventDto eventDto = eventMapper.toEventDto(projectViewDto);
        eventDto.setEventType(PROJECT_VIEW);
        analyticsEventService.saveEvent(eventDto);
    }
}
