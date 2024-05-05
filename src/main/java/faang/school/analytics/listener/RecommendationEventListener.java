package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> {
    public RecommendationEventListener(ObjectMapper objectMapper, AnalyticsEventService analyticsEventService, AnalyticsEventMapper analyticsEventMapper) {
        super(objectMapper, analyticsEventService, analyticsEventMapper, RecommendationEvent.class);
    }

    @Override
    public AnalyticsEvent process(RecommendationEvent event) {
        AnalyticsEvent analyticEvent = analyticsEventMapper.toAnalyticEvent(event);
        analyticEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
        return analyticEvent;
    }
}
