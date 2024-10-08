package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.AnalyticsEventService;
import faang.school.analytics.model.dto.SearchAppearanceEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SearchAppearanceEventListener implements MessageListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            SearchAppearanceEvent event = objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class);
            analyticsEventService.createAnalyticsEvent(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
