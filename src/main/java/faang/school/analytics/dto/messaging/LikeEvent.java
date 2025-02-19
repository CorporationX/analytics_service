package faang.school.analytics.dto.messaging;

import java.time.LocalDateTime;

public record LikeEvent(long postId, long authorId, long userId, LocalDateTime timeStamp) {
}
