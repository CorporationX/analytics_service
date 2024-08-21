package faang.school.analytics.dto;


import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalyticsEventFilterDto {

    @NotNull
    private Long id;

    @NotNull
    private EventType eventType;
    private Interval interval;
    private LocalDateTime start;
    private LocalDateTime end;
}
