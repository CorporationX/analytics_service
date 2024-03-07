package faang.school.analytics.listener;

import faang.school.analytics.dto.FollowerEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public FollowerEventListener(AnalyticsEventMapper analyticsEventMapper, AnalyticsEventService analyticsEventService) {
        super(FollowerEvent.class);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    protected void processEvent(FollowerEvent event) {
        analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(event));
    }
}