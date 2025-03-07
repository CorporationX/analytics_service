package faang.school.analytics.listener;

import faang.school.analytics.dto.AnalyticsEventDto;
import faang.school.analytics.dto.CommentCreateEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventListener {
    private final AnalyticsEventService analyticsEventService;

    @KafkaListener(topics = "${spring.kafka.topics.comment-create}",
            properties = "spring.json.value.default.type=faang.school.analytics.dto.CommentCreateEvent",
            groupId = "${spring.kafka.group-id}"
    )
    public void onCommentCreate(CommentCreateEvent event) {
        sendEvent(event.authorId(), event.postId(), EventType.POST_COMMENT);
    }

    private void sendEvent(Long actorId, Long receiverId, EventType type) {
        AnalyticsEventDto analyticsEvent = AnalyticsEventDto.builder()
                .actorId(actorId)
                .receiverId(receiverId)
                .eventType(type)
                .receivedAt(LocalDateTime.now())
                .build();
        analyticsEventService.saveEvent(analyticsEvent);
    }
}