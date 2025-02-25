package faang.school.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectViewProfileEvent(
        @NotBlank
        Long projectId,
        @NotBlank
        Long userId,
        @NotBlank
        LocalDateTime dateTime) {
}
