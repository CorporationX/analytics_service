package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SearchAppearanceEventListener implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            log.info("Received search appearance message");
            SearchAppearanceEvent event = objectMapper.readValue(message.getBody(), SearchAppearanceEvent.class);
            analyticsEventService.processEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}