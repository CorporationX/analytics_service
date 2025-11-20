package faang.school.analytics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PostViewEvent(
        Long postId,
        Long authorId,
        Long viewerId,
        LocalDateTime currentTime
) {
}
