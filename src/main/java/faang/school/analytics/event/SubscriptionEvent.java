package faang.school.analytics.event;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SubscriptionEvent(Long followerId,
                                Long followeeId,
                                LocalDateTime subscribedAt,
                                String followerName,
                                String followeeName) {
}