package faang.school.analytics.dto;


import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LikeEventDto(
        long postAuthorId,
        long userId,
        LocalDateTime createdAt) {
}
