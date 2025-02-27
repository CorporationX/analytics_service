package faang.school.analytics.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.mapper.EventMapper;
import faang.school.analytics.redis.event.AnalyticsRedisEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnalyticsMessageSubscriber implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final EventMapper eventMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        log.info("Received message: {}", message);
        try {
            AnalyticsRedisEvent event = objectMapper.readValue(
                    message.getBody(),
                    AnalyticsRedisEvent.class
            );

            analyticsEventService.saveEvent(eventMapper.toAnalyticsEvent(event));
        } catch (IOException e) {
            log.error("Error while processing message", e);
        }
    }
}
