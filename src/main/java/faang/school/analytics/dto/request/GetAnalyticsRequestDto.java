package faang.school.analytics.dto.request;

import faang.school.analytics.dto.Interval;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record GetAnalyticsRequestDto(
        @NotNull Long receiverId,
        @NotNull EventType eventType,
        Interval interval,
        LocalDateTime from,
        LocalDateTime to
) {
}
