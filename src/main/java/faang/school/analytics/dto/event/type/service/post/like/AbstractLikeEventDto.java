package faang.school.analytics.dto.event.type.service.post.like;

import faang.school.analytics.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public abstract class AbstractLikeEventDto {
    protected final EventType eventType;
    protected final Long authorId;
    protected final Long userExciterId;
    protected final LocalDateTime createdAt;
}