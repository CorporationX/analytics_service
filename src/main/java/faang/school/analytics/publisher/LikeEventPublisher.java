package faang.school.analytics.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.LikeEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LikeEventPublisher {

    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishLikeEvent(LikeEvent event) {
        String jsonEvent = writeEvent(event);
        applicationEventPublisher.publishEvent(jsonEvent);
        log.info("Like event {} published", event);
    }

    private String writeEvent(LikeEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException exception) {
            log.error("message was not downloaded: {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
