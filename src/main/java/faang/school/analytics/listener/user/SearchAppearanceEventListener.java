package faang.school.analytics.listener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.user.SearchAppearanceEvent;
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
public class SearchAppearanceEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("Received message {}", message);
        try {
            SearchAppearanceEvent event = objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class);
            analyticsEventService.saveProfileView(event);
        } catch (IOException e) {
            log.error("Error reading value from redis", e);
            throw new IllegalStateException(e);
        }
    }
}
