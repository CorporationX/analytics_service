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

    public abstract void setReceiverId(Long receiverId);

    public abstract void setActorId(Long actorId);

    public abstract void setTimestamp(LocalDateTime timestamp);
}
