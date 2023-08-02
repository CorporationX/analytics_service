package faang.school.analytics.service.redis.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsMapper;
import faang.school.analytics.mapper.PremiumEventsMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.analytics.AnalyticsService;
import faang.school.analytics.service.redis.events.PremiumEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class PremiumEventsListener implements MessageListener {
    private final AnalyticsService analyticsService;

    private final AnalyticsMapper analyticsMapper;

    private final PremiumEventsMapper premiumEventsMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ObjectMapper objectMapper = new ObjectMapper();
        PremiumEvent premiumEvent = null;
        try {
            premiumEvent = objectMapper.readValue(message.getBody(), PremiumEvent.class);
            AnalyticsEvent analyticsEvent = premiumEventsMapper.toAnalyticsEvent(premiumEvent);

            analyticsService.create(analyticsMapper.toDto(analyticsEvent));

            log.info("Received message: " + "Premium with id: " + analyticsEvent.getId() + " was " + analyticsEvent.getEventType());
        } catch (IOException e) {
            log.error(e.toString());
            throw new RuntimeException(e);
        }
    }
}
