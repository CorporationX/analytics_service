package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record FollowerEvent(long followerId, long followeeId, LocalDateTime subscriptionDate) {
}