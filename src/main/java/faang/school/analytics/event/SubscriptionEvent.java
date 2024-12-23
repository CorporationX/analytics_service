package faang.school.analytics.event;

import java.time.LocalDateTime;

public record SubscriptionEvent(
        Long followerId,
        Long followeeId,
        LocalDateTime subscribedAt,
        String followerName,
        String followeeName
) {
}