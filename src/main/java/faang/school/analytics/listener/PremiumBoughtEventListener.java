package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.event.PremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> implements MessageListener {
    public PremiumBoughtEventListener(ObjectMapper objectMapper, AnalyticsEventMapper analyticsEventMapper,
                                      AnalyticsEventService analyticsEventService

    ) {
        super(objectMapper, analyticsEventMapper, analyticsEventService);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            PremiumBoughtEvent event = objectMapper.readValue(message.toString(), PremiumBoughtEvent.class);
            sendAnalytics(event);
        } catch (JsonProcessingException e) {
            log.error("Message could not be parsed: {}", message, e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected EventType getEventType() {
        return EventType.PREMIUM_BUYING;
    }
}
