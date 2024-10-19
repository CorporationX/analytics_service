package faang.school.analytics.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public abstract class BaseEvent {
    private final Long receiverId;
    private final Long actorId;
    private final LocalDateTime receivedAt;

    public BaseEvent(Long receiverId, Long actorId, LocalDateTime receivedAt) {
        this.receiverId = receiverId;
        this.actorId = actorId;
        this.receivedAt = receivedAt;
    }
}
