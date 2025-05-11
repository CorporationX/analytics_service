package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.exception.JsonDeserializationException;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
@Slf4j
public class AbstractEventListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsService;

    public <T> void processEvent(String message, Class<T> eventType, Function<T, AnalyticsEventDto> mapper) {
        try {
            log.debug("Received new event: {}", message);
            T event = objectMapper.readValue(message, eventType);
            AnalyticsEventDto analytics = mapper.apply(event);
            analyticsService.saveEvent(analytics);
        } catch (JsonProcessingException e) {
            throw new JsonDeserializationException("Deserialization json %s to event object error", message);
        }
    }
}
