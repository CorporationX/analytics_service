package faang.school.analytics.listener;

import faang.school.analytics.dto.GoalCompletedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    public GoalCompletedEventListener(AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper) {
        super(GoalCompletedEvent.class);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    public void processEvent(GoalCompletedEvent event) {
        analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(event));
    }
}
