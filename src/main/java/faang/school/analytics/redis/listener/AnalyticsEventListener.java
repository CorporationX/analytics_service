package faang.school.analytics.redis.listener;


import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.redis.events.PremiumBoughtEvent;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AnalyticsEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventRepository analyticsEventRepository;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            log.info("Received analytics event: {}", json);

            PremiumBoughtEvent premiumEvent = objectMapper.readValue(json, PremiumBoughtEvent.class);

            AnalyticsEvent analyticsEvent = new AnalyticsEvent(
                    null,
                    premiumEvent.getUserId(),
                    premiumEvent.getAmount(),
                    premiumEvent.getDuration(),
                    premiumEvent.getTimestamp()
            );

            analyticsEventRepository.save(event);
            log.info("Saved analytics event to the database: {}", event);
        } catch (Exception e) {
            log.error("Failed to process analytics event", e);
        }
    }
}