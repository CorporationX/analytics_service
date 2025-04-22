package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.config.redis.RedisListener;
import faang.school.analytics.dto.event.AnalyticDto;
import faang.school.analytics.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RedisListener(topic = "recommendationEvent")
public class RecommendationEventListener extends AbstractEventListener<AnalyticDto> {
    private final AnalyticsEventSaver analyticsEventSaver;

    public RecommendationEventListener(ObjectMapper objectMapper,
                                       AnalyticsEventSaver analyticsEventSaver) {
        super(objectMapper, AnalyticDto.class);
        this.analyticsEventSaver = analyticsEventSaver;
    }

    @Override
    protected void handleEvent(AnalyticDto event) {
        analyticsEventSaver.saveAnalyticsEvent(event, EventType.RECOMMENDATION_RECEIVED);
    }
}