package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.dto.GoalCompletedEvent;
import faang.school.analytics.model.enums.EventType;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedEventListener extends AbstractRedisListener<GoalCompletedEvent> {

    public GoalCompletedEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(GoalCompletedEvent.class, message, this::convertToAnalyticsEvent);
    }

    private AnalyticsEvent convertToAnalyticsEvent(GoalCompletedEvent goalCompletedEvent) {
        return AnalyticsEvent.builder()
                .receiverId(goalCompletedEvent.getReceiverId())
                .actorId(goalCompletedEvent.getActorId())
                .eventType(EventType.GOAL_COMPLETED)
                .receivedAt(goalCompletedEvent.getReceivedAt())
                .build();
    }
}