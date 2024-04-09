package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.analytics.AnalyticsEventDto;
import faang.school.analytics.event.UserPremiumBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.type.SerializationException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumBoughtEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsService analyticsService;
    private final AnalyticsEventMapper analyticsEventMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            UserPremiumBoughtEvent userPremiumBoughtEvent = objectMapper.readValue(message.getBody(), UserPremiumBoughtEvent.class);
            AnalyticsEventDto analyticsEvent = analyticsEventMapper.toDto(userPremiumBoughtEvent);
            analyticsEvent.setEventType(EventType.PREMIUM_BOUGHT);
            analyticsEvent.setReceivedAt(LocalDateTime.now());
            analyticsService.saveEvent(analyticsEvent);
            log.info("Event received: {}", analyticsEvent);
        } catch (IOException e) {
            throw new SerializationException("Failed to deserialize message", e);
        }
    }
}
