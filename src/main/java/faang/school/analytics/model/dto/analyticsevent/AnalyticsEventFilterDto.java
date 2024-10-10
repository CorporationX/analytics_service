package faang.school.analytics.model.dto.analyticsevent;

import faang.school.analytics.model.enums.Interval;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AnalyticsEventFilterDto(
        Interval interval,
        LocalDateTime from,
        LocalDateTime to
) {
}
