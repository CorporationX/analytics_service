package faang.school.analytics.dto;

import java.time.LocalDateTime;

public record FollowerEvent(Long followerId,
                            Long followeeId,
                            Long projectId,
                            LocalDateTime timestamp) {
}