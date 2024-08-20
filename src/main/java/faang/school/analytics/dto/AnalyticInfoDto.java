package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticInfoDto {

    private long receiverId;
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
