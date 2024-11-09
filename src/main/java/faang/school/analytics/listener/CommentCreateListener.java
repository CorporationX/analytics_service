package faang.school.analytics.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.model.event.CommentEvent;
import faang.school.analytics.service.AnalyticsCommentEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommentCreateListener {

    private final AnalyticsCommentEventService service;
    private final ObjectMapper objectMapper;

    private CommentEvent readEvent(String jsonEvent) {
        try {
            System.out.println("readEvent jsonEvent : " + jsonEvent);
            log.info("reading message: {} ", jsonEvent);
            return objectMapper.readValue(jsonEvent, CommentEvent.class);
        } catch (JsonProcessingException exception) {
            log.error("message was not downloaded: {}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

    @EventListener
    public void handleMessage(String jsonEvent) {
        CommentEvent event = readEvent(jsonEvent);
        service.saveCommentEvent(event);
        log.info("Comment event: {} was saved", event);
    }
}
