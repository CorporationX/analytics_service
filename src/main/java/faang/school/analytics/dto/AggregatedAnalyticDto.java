package faang.school.analytics.dto;

import faang.school.analytics.model.Interval;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AggregatedAnalyticDto {

    private LocalDateTime intervalStart;
    private Interval interval;
    private long eventCount;
}
