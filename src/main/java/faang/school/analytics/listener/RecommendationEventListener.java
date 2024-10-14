package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapper;
import faang.school.analytics.model.dto.RecommendationEventDto;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class RecommendationEventListener extends AbstractEventListener<RecommendationEventDto> implements MessageListener {
    public RecommendationEventListener(AnalyticsEventService analyticsEventService,
                                       AnalyticsEventMapper analyticsEventMapper,
                                       ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("message received! {}",message);
        RecommendationEventDto event = handleEvent(message, RecommendationEventDto.class);
        AnalyticsEvent entity = analyticsEventMapper.toEntity(event);
        entity.setEventType(EventType.RECOMMENDATION_RECEIVED);
        analyticsEventService.saveEvent(entity);
    }
}
