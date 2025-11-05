package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AnalyticsEventRequestDto(
        @NotNull(message = "ReceivedId cannot be null")
        long receiverId,
        @NotNull(message = "EventType cannot be null")
        EventType eventType,
        Interval interval,
        LocalDateTime from,
        LocalDateTime to) {
}