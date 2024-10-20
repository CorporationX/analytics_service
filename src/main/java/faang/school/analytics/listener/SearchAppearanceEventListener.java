package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.listener.event.SearchAppearanceEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchAppearanceEventListener extends AbstractEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("New message received: {}", message);
        SearchAppearanceEvent event;
        try {
            event = objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class);
        } catch (IOException e) {
            log.error("Error while parsing message");
            throw new RuntimeException(e);
        }
        analyticsEventService.saveSearchAppearanceEvent(event);
    }

    @Override
    public String getTopic() {
        return RedisTopics.SEARCH_APPEARANCE.getTopic();
    }
}
