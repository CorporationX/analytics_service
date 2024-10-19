package faang.school.analytics.dto.event;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class AbstractEvent {
    protected long actorId;
    protected long receiverId;
    protected LocalDateTime receivedAt;

    public abstract void setActorId(long actorId);

    public abstract void setReceiverId(long receiverId);

    public abstract void setReceivedAt(LocalDateTime receivedAt);
}
