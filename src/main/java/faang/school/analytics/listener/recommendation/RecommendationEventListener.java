package faang.school.analytics.listener.recommendation;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.RecommendationEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEvent> implements MessageListener {

    public RecommendationEventListener(AnalyticsEventService analyticsEventService,
                                       AnalyticsEventMapper analyticsEventMapper,
                                       ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, RecommendationEvent.class, event -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.fromEventClass(event.getClass()));
            analyticsEventService.save(analyticsEvent);
            log.info("Recommendation given: Author ID: {}, Recipient ID: {}, Recommendation ID: {}",
                    event.getAuthorId(),
                    event.getRecipientId(),
                    event.getRecommendationId());
        });
    }
}

