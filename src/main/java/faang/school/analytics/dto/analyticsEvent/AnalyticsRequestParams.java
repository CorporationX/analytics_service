package faang.school.analytics.dto.analyticsEvent;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AnalyticsRequestParams {
    private final EventType eventType;
    private final Interval interval;
    private final LocalDateTime from;
    private final LocalDateTime to;
}
