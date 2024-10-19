package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticsevent.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.FundRaisedEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class FundRaisedEventListener extends AbstractEventListener<FundRaisedEvent> implements MessageListener {

    public FundRaisedEventListener(AnalyticsEventService analyticsEventService,
                                   AnalyticsEventMapper analyticsEventMapper,
                                   ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        FundRaisedEvent event = handleEvent(message, FundRaisedEvent.class);
        AnalyticsEvent entity = analyticsEventMapper.toEntity(event);
        entity.setEventType(EventType.FUND_RAISED);
        analyticsEventService.saveEvent(entity);
    }
}
