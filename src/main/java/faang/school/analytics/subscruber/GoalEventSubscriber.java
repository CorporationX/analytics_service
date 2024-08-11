package faang.school.analytics.subscruber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.Message;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class GoalEventSubscriber extends RedisAbstractMessageSubscriber{
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        try {
            GoalCompletedEvent goalEvent = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.goalCompletedToAnalyticsEvent(goalEvent);
            analyticsEventService.saveEventEntity(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
