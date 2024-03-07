package faang.school.analytics.listener;

import faang.school.analytics.dto.premium.PremiumEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.stereotype.Component;

@Component
public class PremiumEventListener extends AbstractEventListener<PremiumEvent> {
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public PremiumEventListener(AnalyticsEventMapper analyticsEventMapper, AnalyticsEventService analyticsEventService) {
        super(PremiumEvent.class);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    protected void processEvent(PremiumEvent event) {
        analyticsEventService.saveEvent(analyticsEventMapper.toAnalyticsEvent(event));
    }
}