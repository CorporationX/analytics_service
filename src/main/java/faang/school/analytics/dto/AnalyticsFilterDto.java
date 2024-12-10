package faang.school.analytics.dto;

import faang.school.analytics.filter.Interval;
import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsFilterDto {
    @NotNull
    private long receiverId;
    @NotNull
    private EventType eventType;
    private Interval interval;
    @NotNull
    private LocalDateTime from;
    @NotNull
    private LocalDateTime to;
}
