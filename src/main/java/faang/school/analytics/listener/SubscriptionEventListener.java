package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SubscriptionEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        SubscriptionEvent subscriptionEvent;

        try {
            subscriptionEvent = objectMapper.readValue(message.getBody(), SubscriptionEvent.class);
        } catch (IOException e) {
            log.error("Failed to deserialize message body: {}. Error: {}", messageBody, e.getMessage(), e);
            return;
        }

        AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(subscriptionEvent);
        AnalyticsEvent savedEvent = analyticsEventService.saveEvent(analyticsEvent);
        log.info("Successfully processed subscription event. AnalyticsEvent ID: {}, Source Message: {}",
                savedEvent.getId(), messageBody);
    }
}