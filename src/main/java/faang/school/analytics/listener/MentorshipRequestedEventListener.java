package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.dto.MentorshipRequestedEvent;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.impl.AnalyticsEventServiceImpl;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MentorshipRequestedEventListener extends AbstractRedisListener<MentorshipRequestedEvent> {

    public MentorshipRequestedEventListener(ObjectMapper objectMapper, AnalyticsEventServiceImpl analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(MentorshipRequestedEvent.class, message, this::convertToAnalyticsEvent);
    }

    private AnalyticsEvent convertToAnalyticsEvent(MentorshipRequestedEvent mentorshipRequestedEvent) {
        return AnalyticsEvent.builder()
                .receiverId(mentorshipRequestedEvent.getReceiverId())
                .actorId(mentorshipRequestedEvent.getActorId())
                .eventType(EventType.MENTORSHIP_REQUEST)
                .receivedAt(LocalDateTime.now())
                .build();
    }
}
