package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.LikeEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikeEventListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;


    @EventListener
    public void handleMessage(String jsonEvent) {
        LikeEvent event = readEvent(jsonEvent);
        analyticsEventService.saveLikeEvent(event);
    }

    private LikeEvent readEvent(String jsonEvent) {
        try {
            return objectMapper.readValue(jsonEvent, LikeEvent.class);
        } catch (JsonProcessingException exception) {
            throw new RuntimeException(exception);
        }
    }
}
