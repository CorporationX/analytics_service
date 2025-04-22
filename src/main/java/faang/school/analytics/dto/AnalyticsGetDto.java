package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.service.Interval;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsGetDto {
    Long receiverId;
    EventType eventType;
    Interval interval;
    LocalDateTime from;
    LocalDateTime to;
}
