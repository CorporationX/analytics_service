package faang.school.analytics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsRequest {
    private long receiverId;
    private EventType type;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
