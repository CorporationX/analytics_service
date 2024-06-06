package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.service.AnalyticsService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedListener extends AbstractEventListener<GoalCompletedEvent> {

    private final AnalyticsService<GoalCompletedEvent> analyticsService;

    public GoalCompletedListener(ObjectMapper objectMapper, AnalyticsService<GoalCompletedEvent> analyticsService) {
        super(objectMapper);
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, GoalCompletedEvent.class, analyticsService::save);
    }
}
