package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.service.Interval;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventRequestDto {
    private Long receiverId;
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
