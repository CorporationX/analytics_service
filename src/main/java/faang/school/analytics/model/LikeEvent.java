package faang.school.analytics.model;

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
    private LocalDateTime timestamp;
    private EventType eventType = EventType.of(5);
}
