package faang.school.analytics.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.RecommendationEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.NonNull;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class RecommendationListener extends AbstractListenerHandler<RecommendationEvent>{

    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    public RecommendationListener(ObjectMapper objectMapper,
                                  AnalyticsEventMapper analyticsEventMapper,
                                  AnalyticsEventService analyticsEventService) {
        super(objectMapper);
        this.analyticsEventMapper = analyticsEventMapper;
        this.analyticsEventService = analyticsEventService;
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handler(message, RecommendationEvent.class, event -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
            analyticsEventService.saveEvent(analyticsEvent);
        });
    }
}
