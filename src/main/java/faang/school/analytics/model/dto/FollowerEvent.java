package faang.school.analytics.model.dto;

import java.time.LocalDateTime;

public record FollowerEvent(long followerId, long followeeId, LocalDateTime receivedAt) {
}