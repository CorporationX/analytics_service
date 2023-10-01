package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.RecommendationEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEventDto> {

    @Autowired
    public RecommendationEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsMapper, AnalyticsEventService analyticsEventService) {
        super(objectMapper, analyticsMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        RecommendationEventDto event = convertJsonToString(message, RecommendationEventDto.class);
        AnalyticsEvent analyticsEvent = super.analyticsMapper.toEntity(event);
        analyticsEvent.setEventType(EventType.RECOMMENDATION_RECEIVED);
        super.analyticsEventService.create(analyticsEvent);
    }
}