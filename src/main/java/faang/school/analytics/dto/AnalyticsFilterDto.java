package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalyticsFilterDto {

    @NotNull(message = "ReceiverId can't be null")
    private Long receiverId;
    @NotNull(message = "EventType can't be null")
    private EventType eventType;
    private Interval interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
