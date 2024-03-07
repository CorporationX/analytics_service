package faang.school.analytics.listener;

import faang.school.analytics.dto.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.hibernate.event.spi.AbstractEvent;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestedEventListener extends AbstractEventListener<MentorshipRequestedEvent> {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;

    public MentorshipRequestedEventListener(AnalyticsEventService analyticsEventService,
                                            AnalyticsEventMapper analyticsEventMapper) {
        super(MentorshipRequestedEvent.class);
        this.analyticsEventService = analyticsEventService;
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    protected void processEvent(MentorshipRequestedEvent event) {
        AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
        analyticsEventService.saveEvent(analyticsEvent);
    }
}