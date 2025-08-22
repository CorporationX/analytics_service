package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.service.Interval;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAnalyticsDto {
    @Positive
    @NotNull
    private long receiverId;
    @NotNull
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
