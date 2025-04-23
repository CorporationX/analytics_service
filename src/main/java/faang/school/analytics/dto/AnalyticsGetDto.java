package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsGetDto {

    Long receiverId;
    @NotNull
    EventType eventType;
    @NotNull
    Interval interval;
    LocalDateTime from;
    LocalDateTime to;
}
