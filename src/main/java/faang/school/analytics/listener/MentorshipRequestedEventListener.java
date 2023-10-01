package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.MentorshipRequestEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;


@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestEvent> {

    public MentorshipRequestedEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestEvent event = convertJsonToString(message, MentorshipRequestEvent.class);
        AnalyticsEvent analyticsEvent = analyticsMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.MENTORSHIP_REQUEST);
        analyticsEventService.create(analyticsEvent);
    }
}
