package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnalyticsIntervalDto {
    private long receiverId;
    @NotNull
    private EventType eventType;
    String interval;
    LocalDateTime from;
    LocalDateTime to;
}
