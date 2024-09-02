package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.publishable.MentorshipRequestedEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class MentorshipRequestsEventListener extends AbstractEventListener<MentorshipRequestedEvent> {
    private final AnalyticsEventMapper analyticsEventMapper;

    public MentorshipRequestsEventListener(ObjectMapper objectMapper,
                                           AnalyticsEventService analyticsEventService,
                                           AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, MentorshipRequestedEvent.class);
        this.analyticsEventMapper = analyticsEventMapper;
    }

    @Override
    protected AnalyticsEvent toAnalyticsEvent(MentorshipRequestedEvent dto) {
        return analyticsEventMapper.toEntity(dto);
    }
}
