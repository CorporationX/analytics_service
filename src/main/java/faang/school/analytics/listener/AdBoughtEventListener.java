package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AdBoughtEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdBoughtEventListener extends AbstractEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received AdBought event: {}", message.toString());
        AdBoughtEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), AdBoughtEvent.class);
        } catch (IOException e) {
            log.error("Error while parsing message: {}", message);
            throw new RuntimeException(e);
        }
        analyticsEventService.saveAdBoughtEvent(event);
    }

    @Override
    public String getTopic() {
        return RedisTopics.AD_BOUGHT_EVENT.getTopic();
    }
}
