package faang.school.analytics.model.event.type.service.post.like;

import faang.school.analytics.model.event.type.AnalyticsEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public abstract class AbstractLikeEvent {
    protected final Long likedEntityId;
    protected final Long authorId;
    protected final Long userExciterId;
    protected final LocalDateTime createdAt = LocalDateTime.now();

    public abstract AnalyticsEvent createAnalyticsEvent();
}