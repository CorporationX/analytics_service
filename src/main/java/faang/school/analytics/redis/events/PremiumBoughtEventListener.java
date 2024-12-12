package faang.school.analytics.redis.events;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.apache.logging.log4j.message.Message;
//import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.data.redis.connection.Message;
//import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class PremiumBoughtEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = new String(message.getBody());
            log.info("Received Redis message: {}", json);

            PremiumBoughtEvent premiumEvent = objectMapper.readValue(json, PremiumBoughtEvent.class);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.map(premiumEvent);
            analyticsEventService.saveEvent(analyticsEvent);

            log.info("Saved analytics event to the database: {}", analyticsEvent);
        } catch (Exception e) {
            log.error("Failed to process Redis message", e);
        }
    }
}