package faang.school.analytics.listener.premium;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.premium.PremiumBoughtEvent;
import faang.school.analytics.mapper.premium.PremiumBoughtEventMapper;
import faang.school.analytics.service.events.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class PremiumBoughtEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final PremiumBoughtEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message {}", message);
        try {
            PremiumBoughtEvent premiumBoughtEvent = objectMapper.readValue(message.getBody(), PremiumBoughtEvent.class);
            analyticsEventService.saveEvent(analyticsEventMapper.premiumBoughtToAnalyticsDto(premiumBoughtEvent));
        } catch (IOException e) {
            log.error("Error reading value from redis", e);
            throw new IllegalStateException(e);
        }
    }
}
