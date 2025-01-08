package faang.school.analytics.listener.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.domain.dto.events.analytic.AnalyticsEventDto;
import faang.school.analytics.service.events.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdBoughtEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received message: {}", message);
            AnalyticsEventDto event = objectMapper.readValue(message.getBody(), AnalyticsEventDto.class);
            analyticsEventService.saveEvent(event);
        } catch (IOException e) {
            log.error("Error reading value from redis", e);
            throw new IllegalStateException(e);
        }
    }
}
