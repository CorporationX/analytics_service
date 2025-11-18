package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.mapper.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentEventListener {
    private final ObjectMapper objectMapper;
    private final AnalyticsEventMapper analyticsEventMapper;
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${app.kafka.topics.comment-events}")
    public void handleCommentEvent(String jsonEvent) {
        try {
            CommentEventDto commentEventDto = objectMapper.readValue(jsonEvent, CommentEventDto.class);
            log.debug("Successfully listen event from a comment-events topic: {}", jsonEvent);
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toEntity(commentEventDto);
            analyticsEventService.saveEvent(analyticsEvent);
            log.debug("Comment create event saved: {}", analyticsEvent);
        } catch (JsonParseException exception) {
            log.error("Bad JSON syntax: {}", jsonEvent);
        } catch (JsonMappingException exception) {
            log.error("Incorrect structure of JSON: {}", jsonEvent);
        } catch (Exception exception) {
            log.error("Failed to process or to create comment event {}", jsonEvent, exception);
        }
    }
}
