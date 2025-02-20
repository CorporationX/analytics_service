package faang.school.analytics.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentCreateEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentKafkaConsumer {
    private final AnalyticsEventService analyticsEventService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.comment_create_event_topic_name}", groupId = "group-id")
    public void onCommentCreate(String message) {
        CommentCreateEvent commentCreateEvent;
        try {
            commentCreateEvent = objectMapper.readValue(message, CommentCreateEvent.class);
        } catch (JsonProcessingException e) {
            log.error("couldn't convert json to String: " + e);
            throw new RuntimeException("couldn't convert json to String" + e.getMessage());
        }

        AnalyticsEventDto analyticsEvent = AnalyticsEventDto.builder()
                .eventType(EventType.POST_COMMENT)
                .actorId(commentCreateEvent.authorId())
                .receiverId(commentCreateEvent.postId())
                .receivedAt(LocalDateTime.now())
                .build();
        analyticsEventService.saveEvent(analyticsEvent);
    }
}
