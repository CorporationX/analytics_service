package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeEventListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;

    @EventListener
    public void handleMessage(String jsonEvent) {
        LikeEvent event = readEvent(jsonEvent);
        analyticsEventService.saveLikeEvent(event);
        log.info("Like event: {} was saved", event);
    }

    private LikeEvent readEvent(String jsonEvent) {
        try {
            log.info("reading message: {} ", jsonEvent);
            return objectMapper.readValue(jsonEvent, LikeEvent.class);
        } catch (JsonProcessingException exception) {
            log.error("message was not downloaded: {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
