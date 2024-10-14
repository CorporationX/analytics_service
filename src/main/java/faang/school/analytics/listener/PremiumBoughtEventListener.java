package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.analyticevent.AnalyticsEventMapper;
import faang.school.analytics.model.entity.AnalyticsEvent;
import faang.school.analytics.model.enums.EventType;
import faang.school.analytics.model.event.PremiumBoughtEvent;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> implements MessageListener {

    public PremiumBoughtEventListener(AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper,
                                      ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        PremiumBoughtEvent premiumBoughtEvent = handleEvent(message, PremiumBoughtEvent.class);
        AnalyticsEvent event = analyticsEventMapper.toEntity(premiumBoughtEvent);
        event.setEventType(EventType.PREMIUM_BOUGHT);
        analyticsEventService.saveEvent(event);
    }
}
