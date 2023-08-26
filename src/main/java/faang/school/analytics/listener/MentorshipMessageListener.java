package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.mapper.JsonObjectMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorshipMessageListener implements MessageListener {

    private final JsonObjectMapper jsonObjectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        AnalyticsEventDto eventDto = jsonObjectMapper.readValue(message.getBody(), AnalyticsEventDto.class);
        eventDto.setEventType(EventType.PROJECT_INVITE);
        analyticsEventService.saveEvent(eventDto);
    }
}
