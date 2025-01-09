package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FollowerEvent(
        Long followerId,
        Long followeeId,
        Long projectId,
        LocalDateTime eventTime
) {
}
