package faang.school.analytics.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.sql.Timestamp;

@Builder
public record LikeEventDto(
        @NotBlank
        long postId,
        @NotBlank
        long postAuthorId,
        @NotBlank
        long userId,
        @NotBlank
        Timestamp timestamp) {
}
