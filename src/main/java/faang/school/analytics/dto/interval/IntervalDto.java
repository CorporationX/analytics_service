package faang.school.analytics.dto.interval;

import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record IntervalDto(
        @PastOrPresent LocalDateTime start,
        LocalDateTime end
) {
}
