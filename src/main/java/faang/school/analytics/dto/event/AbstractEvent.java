package faang.school.analytics.dto.event;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public abstract class AbstractEvent {
    private long actorId;
    private long receiverId;
    private LocalDateTime receivedAt;
}
