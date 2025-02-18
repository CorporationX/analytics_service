package faang.school.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Builder
public record FollowerEventDto (
    @NotBlank
    long followerId,
    @NotBlank
    long followeeId,
    @NotBlank
    LocalDateTime timestamp) {}

