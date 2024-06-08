package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedListener extends AbstractEventListener<GoalCompletedEvent> {

    private final AnalyticsEventMapper mapper;
    private final AnalyticsService analyticsService;

    public GoalCompletedListener(ObjectMapper objectMapper, AnalyticsEventMapper mapper, AnalyticsService analyticsService) {
        super(objectMapper);
        this.mapper = mapper;
        this.analyticsService = analyticsService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, GoalCompletedEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
            analyticsService.save(analyticsEvent);
        });
    }
}
