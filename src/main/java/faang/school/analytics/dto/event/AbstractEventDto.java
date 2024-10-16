package faang.school.analytics.dto.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public abstract class AbstractEventDto {
    protected Long receiverId;
    protected Long actorId;
    protected LocalDateTime timestamp;

    abstract void setReceiverId(Long receiverId);

    abstract void setActorId(Long actorId);

    abstract void setTimestamp(LocalDateTime timestamp);
}
