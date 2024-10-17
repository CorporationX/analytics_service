package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevents.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.MentorshipRequestedEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEvent> implements MessageListener {
    public MentorshipRequestedEventListener(AnalyticsEventService analyticsEventService,
                                            AnalyticsEventMapper analyticsEventMapper,
                                            ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        MentorshipRequestedEvent event = handleEvent(message, MentorshipRequestedEvent.class);
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.MENTORSHIP_REQUEST);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
