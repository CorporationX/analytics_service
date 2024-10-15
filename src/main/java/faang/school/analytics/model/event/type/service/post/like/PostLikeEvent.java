package faang.school.analytics.model.event.type.service.post.like;

import faang.school.analytics.model.event.type.AnalyticsEvent;
import faang.school.analytics.model.event.type.EventType;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

public class PostLikeEvent extends AbstractLikeEvent {

    @Builder
    public PostLikeEvent(@Positive Long likedEntityId,
                         @Positive Long authorId,
                         @Positive Long userExciterId) {
        super(likedEntityId, authorId, userExciterId);
    }

    public long getPostId() {
        return getLikedEntityId();
    }

    @Override
    public AnalyticsEvent createAnalyticsEvent() {
        return AnalyticsEvent.builder()
                .receiverId(likedEntityId)
                .actorId(authorId)
                .eventType(EventType.fromEvent(this))
                .receivedAt(createdAt)
                .build();
    }
}
