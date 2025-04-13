package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisListener;
import faang.school.analytics.dto.event.RecommendationEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RedisListener(topic = "recommendationEvent")
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent>{

    public RecommendationEventListener(ObjectMapper objectMapper,
                                       AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsEventService, RecommendationEvent.class);
    }

    @Override
    protected void handleEvent(RecommendationEvent event) {
        saveAnalyticsEvent(event.getAuthorId(), event.getReceiverId(), EventType.RECOMMENDATION_RECEIVED);
    }
}
