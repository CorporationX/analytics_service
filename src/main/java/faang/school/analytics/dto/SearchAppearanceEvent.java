package faang.school.analytics.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SearchAppearanceEvent(
        @NotNull(message = "Author id is required")
        Long authorId,
        @NotNull(message = "Receiver id is required")
        Long receiverId,
        LocalDateTime viewTime
) {
}