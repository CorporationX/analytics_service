package faang.school.analytics.subscruber;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class GoalEventListener extends RedisAbstractMessageListener<GoalCompletedEvent> {
    public GoalEventListener(AnalyticsEventMapper mapper,
                             AnalyticsEventService analyticsEventService,
                             ObjectMapper objectMapper) {
        super(mapper, analyticsEventService, objectMapper, GoalCompletedEvent.class);
    }


    @Override
    AnalyticsEvent map(GoalCompletedEvent goalCompletedEvent) {
        return mapper.toAnalyticsEvent(goalCompletedEvent);
    }
}
