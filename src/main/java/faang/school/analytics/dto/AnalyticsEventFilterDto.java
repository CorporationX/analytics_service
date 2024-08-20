package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class AnalyticsEventFilterDto {
    private long receiverId;

    @NonNull
    private EventType eventType;
    private String interval;
    private LocalDateTime from;
    private LocalDateTime to;
}
