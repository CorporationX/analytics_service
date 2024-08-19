package faang.school.analytics.event;

import faang.school.analytics.model.EventType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
public class LikeEvent {
    private long postId;
    private long authorId;
    private long userId;
    private EventType eventType = EventType.of(5);
    private LocalDateTime receivedAt;
}
