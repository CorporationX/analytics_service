package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.joda.time.Interval;
import org.joda.time.LocalDateTime;

@Data
@Builder
public class AnalyticsEventFilterDto {
    private long receiverId;

    @NonNull
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
