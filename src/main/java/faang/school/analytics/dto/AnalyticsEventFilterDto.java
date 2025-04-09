package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventFilterDto(

        @NotNull(message = "Receiver id cannot be null")
        Long receiverId,

        @NotNull(message = "Event type cannot be null")
        EventType eventType,

        Interval interval,
        LocalDateTime from,
        LocalDateTime to
) {
}
