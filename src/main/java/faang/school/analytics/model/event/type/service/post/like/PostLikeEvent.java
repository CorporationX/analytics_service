package faang.school.analytics.model.event.type.service.post.like;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import faang.school.analytics.model.event.type.AnalyticsEvent;
import faang.school.analytics.model.event.type.EventType;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.time.LocalDateTime;

public class PostLikeEvent extends AbstractLikeEvent {

    @JsonCreator
    @Builder
    public PostLikeEvent(
            @JsonProperty("likedEntityId") @Positive Long likedEntityId,
            @JsonProperty("authorId") @Positive Long authorId,
            @JsonProperty("userExciterId") @Positive Long userExciterId,
            @JsonProperty("createdAt") LocalDateTime createdAt) {
        super(likedEntityId, authorId, userExciterId, createdAt);
    }

    @Override
    public AnalyticsEvent createAnalyticsEvent() {
        if (likedEntityId == null || authorId == null || createdAt == null) {
            throw new IllegalArgumentException("Fields in like event cannot be null");
        }
        return AnalyticsEvent.builder()
                .receiverId(likedEntityId)
                .actorId(authorId)
                .eventType(EventType.fromEvent(this))
                .receivedAt(createdAt)
                .build();
    }
}
