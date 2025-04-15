package faang.school.analytics.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PostViewEvent(
        Long postId,
        Long userId,
        Long authorId,
        LocalDateTime date) {
}

