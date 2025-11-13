package faang.school.analytics.mapper;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.dto.CommentEventDto;
import faang.school.analytics.dto.EventDto;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class AnalyticsEventMapper {

    public AnalyticsEvent toEntity(Object eventDto) {
        if (eventDto instanceof EventDto subscriptionEvent) {
            return mapSubscriptionEvent(subscriptionEvent);
        } else if (eventDto instanceof CommentEventDto commentEvent) {
            return mapCommentEvent(commentEvent);
        }
        log.error("Unsupported event DTO type: {}", eventDto.getClass().getName());
        throw new IllegalArgumentException("Unsupported event DTO type: " + eventDto.getClass());
    }

    private AnalyticsEvent mapSubscriptionEvent(EventDto event) {
        return AnalyticsEvent.builder()
                .receiverId(event.receiverId())
                .actorId(event.actorId())
                .eventType(EventType.valueOf(event.eventType())) // FOLLOWER
                .receivedAt(LocalDateTime.now())
                .postId(null)
                .commentId(null)
                .build();
    }

    private AnalyticsEvent mapCommentEvent(CommentEventDto event) {
        return AnalyticsEvent.builder()
                .receiverId(event.postAuthorId())
                .actorId(event.actorId())
                .eventType(EventType.POST_COMMENT)
                .receivedAt(event.createdAt())
                .postId(event.postId())
                .commentId(event.commentId())
                .build();
    }

    public AnalyticsEventResponseDto toDto(AnalyticsEvent analyticsEvent) {
        return new AnalyticsEventResponseDto(
                analyticsEvent.getActorId(),
                analyticsEvent.getReceiverId(),
                analyticsEvent.getEventType(),
                analyticsEvent.getReceivedAt()
        );
    }

    public List<AnalyticsEventResponseDto> toDtoList(List<AnalyticsEvent> events) {
        return events.stream().map(this::toDto).toList();
    }
}
