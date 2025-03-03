package faang.school.analytics.queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.SearchAppearanceEvent;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchAppearanceEventListener implements MessageListener {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String json = objectMapper.readValue(message.getBody(), String.class);
            System.out.println("AnalyticsEventService1 " + json);
            SearchAppearanceEvent event = objectMapper.readValue(json, SearchAppearanceEvent.class);
            analyticsEventService.saveSearchAppearanceEvent(event);
        } catch (Exception e) {
            log.error("error when saving analytics", e);
        }
    }
}
