package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProfileViewEvent(
        Long profileId,
        Long viewerId,
        LocalDateTime viewedAt
) {
}